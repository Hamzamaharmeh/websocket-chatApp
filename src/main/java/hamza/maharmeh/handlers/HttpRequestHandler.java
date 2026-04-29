package hamza.maharmeh.handlers;

import hamza.maharmeh.database.dao.UserDao;
import hamza.maharmeh.exceptions.NoSuchUserException;
import hamza.maharmeh.model.MessageFactory;
import hamza.maharmeh.model.User;
import hamza.maharmeh.model.inbound.IdMessage;
import hamza.maharmeh.model.outbound.ErrorMessage;
import hamza.maharmeh.model.outbound.UserErrorMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import tools.jackson.databind.ObjectMapper;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private UserDao userDao;
    private String username;

    public HttpRequestHandler(String wsUri,UserDao userDao) {
        this.wsUri = wsUri;
        this.userDao = userDao;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if(wsUri.equalsIgnoreCase(request.uri()) && username != null) {

            AttributeKey<String> user =  AttributeKey.valueOf("user");
            ctx.channel().attr(user).set(username);
            RegisteredUsers.addChannel(ctx.channel());
            ctx.fireChannelRead(request.retain());

        }else if(request.uri().equals("/users/register") && request.method().equals(HttpMethod.POST)) {

            String body = request.content().toString(CharsetUtil.UTF_8);
            IdMessage message = MessageFactory.getMessageType(body,IdMessage.class);

            if(userDao.doesExist(message.sender())){
                ErrorMessage errorMessage = new UserErrorMessage("User already exists");
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.CONFLICT,
                        DefaultHttpHeadersFactory.headersFactory().newEmptyHeaders().isEmpty()
                );
                ByteBuf b = response.content();
                ObjectMapper objectMapper = new ObjectMapper();
                b.writeBytes(objectMapper.writeValueAsBytes(errorMessage));
                ctx.writeAndFlush(response);
                b.release();
            }else {
                User u = new User(message.sender(),message.password());
                this.username = message.sender();
                userDao.save(u);
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        DefaultHttpHeadersFactory.headersFactory().newEmptyHeaders().isEmpty()
                );
                ctx.writeAndFlush(response);
            }
        }else if(request.uri().equals("/users/login") && request.method().equals(HttpMethod.POST)) {

            String body = request.content().toString(CharsetUtil.UTF_8);
            IdMessage message = MessageFactory.getMessageType(body,IdMessage.class);
            try{
                User storedUser = userDao.get(message.sender());
                if(storedUser.password().equals(User.encodePassword(message.password()))) {
                    this.username = message.sender();
                    ctx.writeAndFlush(
                            new DefaultFullHttpResponse(
                                    HttpVersion.HTTP_1_1,
                                    HttpResponseStatus.OK
                            )
                    );
                }else {
                    ctx.writeAndFlush(
                            new DefaultFullHttpResponse(
                                    HttpVersion.HTTP_1_1,
                                    HttpResponseStatus.BAD_REQUEST
                            )
                    );
                }
            }catch(NoSuchUserException e){
                ctx.writeAndFlush(
                        new DefaultFullHttpResponse(
                                HttpVersion.HTTP_1_1,
                                HttpResponseStatus.NOT_FOUND
                        )
                );
            }
        }else {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
        }
    }
    private void register(String requestBody) {

    }
}
