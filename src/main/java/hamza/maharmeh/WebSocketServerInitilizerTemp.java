package hamza.maharmeh;

import hamza.maharmeh.handlers.ChatMessageHandler;
import hamza.maharmeh.handlers.ExcptionHandler;
import hamza.maharmeh.handlers.IdMessageHandler;
import hamza.maharmeh.model.BaseMessage;
import hamza.maharmeh.model.Message;
import hamza.maharmeh.model.MessageFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import tools.jackson.databind.ObjectMapper;

public class WebSocketServerInitilizerTemp extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        final AttributeKey<String> username = AttributeKey.valueOf("username");
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                        .allowedRequestMethods(HttpMethod.GET,HttpMethod.POST,HttpMethod.OPTIONS)
                        .allowedRequestHeaders("*")
                        .build();
        ch.pipeline().addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                new CorsHandler(corsConfig),
                new WebSocketServerProtocolHandler("/websocket"),
                new TextFrameHandler(),
                new IdMessageHandler(),
                new ChatMessageHandler(),
                new ExcptionHandler()
        );
    }

    public static final class TextFrameHandler
            extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            BaseMessage message = MessageFactory.getMessage(msg.text());
//            msg.release();
            ctx.fireChannelRead(message);
        }
    }


}