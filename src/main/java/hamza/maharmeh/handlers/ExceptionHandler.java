package hamza.maharmeh.handlers;

import hamza.maharmeh.exceptions.NoSuchUserException;
import hamza.maharmeh.exceptions.UserNotIdentifiedException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause instanceof UserNotIdentifiedException) {
            ctx.writeAndFlush(new TextWebSocketFrame("User not identified"));
        }else if(cause instanceof NoSuchUserException e) {
            ctx.writeAndFlush(new TextWebSocketFrame("User " + e.user() + " not found"));
            System.out.println("No such user");
        }else {
            System.out.println("Exception caught " + cause);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        RegisteredUsers.removeChannel(ctx.channel());
    }
}
