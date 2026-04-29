package hamza.maharmeh.handlers;

import hamza.maharmeh.exceptions.NoSuchUserException;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

public class RegisteredUsers {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void addChannel(Channel ch) {
        group.add(ch);
    }

    public static Channel getChannel(String user) throws NoSuchUserException {
        for(Channel ch : group){
            if(ch.attr(AttributeKey.valueOf("user")).get().equals(user)) {
                return ch;
            }
        }
        throw new NoSuchUserException(user);
    }
}
