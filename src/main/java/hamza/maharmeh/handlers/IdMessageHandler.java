package hamza.maharmeh.handlers;

import hamza.maharmeh.exceptions.UserNotIdentifiedException;
import hamza.maharmeh.model.BaseMessage;
import hamza.maharmeh.model.IdMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class IdMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof IdMessage m){
            AttributeKey<String> user =  AttributeKey.valueOf("user");
            ctx.channel().attr(user).set(m.sender());
            RegisteredUsers.addChannel(ctx.channel());
            ctx.pipeline().remove(this);
        }else {
            throw new UserNotIdentifiedException();
        }
    }
}
