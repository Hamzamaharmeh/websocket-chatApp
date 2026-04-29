package hamza.maharmeh;

import hamza.maharmeh.database.dao.UserDao;
import hamza.maharmeh.handlers.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;

public class WebSocketServerInitilizer extends ChannelInitializer<Channel> {

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
                new HttpRequestHandler("/websocket",new UserDao()),
                new WebSocketServerProtocolHandler("/websocket"),
                new TextFrameHandler(),
                new ChatMessageHandler(),
                new ExceptionHandler()
        );
    }



}