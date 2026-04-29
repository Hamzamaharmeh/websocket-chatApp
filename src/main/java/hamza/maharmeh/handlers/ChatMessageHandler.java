package hamza.maharmeh.handlers;

import hamza.maharmeh.model.inbound.ChatMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        Channel receiverChannel = RegisteredUsers.getChannel(msg.receiver());

        // Do I need this ???
        receiverChannel.writeAndFlush(new TextWebSocketFrame(msg.content()));
    }
}
