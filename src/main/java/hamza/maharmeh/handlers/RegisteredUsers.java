package hamza.maharmeh.handlers;

import hamza.maharmeh.exceptions.NoSuchUserException;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class RegisteredUsers {

    private static ConcurrentHashMap<String,Channel> usersMap = new ConcurrentHashMap<>();
    private final static AttributeKey<String> USER_ID = AttributeKey.valueOf("user");

    public static void addChannel(Channel ch) {
        usersMap.put(ch.attr(USER_ID).get(), ch);
    }

    public static Channel getChannel(String user) throws NoSuchUserException {
        if(usersMap.contains(user)) {
            return usersMap.get(user);
        }else {
            throw new NoSuchUserException(user);
        }
    }

    public static void removeChannel(String user) {
        usersMap.remove(user);
    }
    public static void removeChannel(Channel ch) {
        usersMap.remove(ch.attr(USER_ID).get());
    }
}
