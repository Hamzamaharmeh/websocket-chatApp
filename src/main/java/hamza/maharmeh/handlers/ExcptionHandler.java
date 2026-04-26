package hamza.maharmeh.handlers;

import hamza.maharmeh.exceptions.NoSuchUserException;
import hamza.maharmeh.exceptions.UserNotIdentifiedException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ExcptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause instanceof UserNotIdentifiedException) {
//            ctx.writeAndFlush()
            System.out.println("Use nto identified");
        }else if(cause instanceof NoSuchUserException e) {
            System.out.println("No such user");
        }else {
            System.out.println("Exception caught " + cause);
        }
    }
}
