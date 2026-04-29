package hamza.maharmeh;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hamza.maharmeh.database.dao.UserDao;
import hamza.maharmeh.handlers.HttpRequestHandler;
import hamza.maharmeh.model.MessageFactory;
import hamza.maharmeh.model.User;
import hamza.maharmeh.model.inbound.IdMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HttpRequestHandlerTest {

    private static DataSource dataSource = new HikariDataSource(
            new HikariConfig("datasource.test.properties")
    );
    private static IdMessage idmessage = new IdMessage("hamza","123");
    private static String registerUrl = "/users/register";
    private static String loginUrl = "/users/login";
    @Test
    @DisplayName("verify registration succeeds, when given valid id message, return http ok")
    public void validRegisterTest() {
        EmbeddedChannel channel = new  EmbeddedChannel(new HttpRequestHandler("/websocket",new UserDao(dataSource)));

        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,
                HttpMethod.POST,
                registerUrl
        );
        String jsonPayload = MessageFactory.toJson(idmessage);
        request.content().writeCharSequence(jsonPayload, CharsetUtil.UTF_8);
        HttpUtil.setContentLength(request, request.content().readableBytes());

        channel.writeInbound(request);
        FullHttpResponse response = channel.readOutbound();
        assertEquals(HttpResponseStatus.OK, response.status());
    }

    @Test
    @DisplayName("verify registration fails,"
    + "given an id message of a user that already exists, return Http conflict")
    public void failedRegistration() {

        UserDao userDao = new UserDao(dataSource);
        EmbeddedChannel channel = new EmbeddedChannel(new HttpRequestHandler("/websocket",userDao));
        userDao.save(new User(idmessage.sender(),idmessage.password()));
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,
                HttpMethod.POST,
                registerUrl
        );
        request.content().writeCharSequence(MessageFactory.toJson(idmessage), CharsetUtil.UTF_8);
        HttpUtil.setContentLength(request, request.content().readableBytes());
        channel.writeInbound(request);
        FullHttpResponse response = channel.readOutbound();


        assertEquals(HttpResponseStatus.CONFLICT,response.status());
    }

    @Test
    @DisplayName("verify login succeeds, given correct username and password, return http OK")
    public void loginTest() {
        UserDao userDao = new UserDao(dataSource);
        userDao.save(new User(idmessage.sender(),idmessage.password()));
        EmbeddedChannel channel = new EmbeddedChannel(new HttpRequestHandler("/websocket",userDao));

        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,
                HttpMethod.POST,
                loginUrl
        );
        request.content().writeCharSequence(MessageFactory.toJson(idmessage), CharsetUtil.UTF_8);
        HttpUtil.setContentLength(request, request.content().readableBytes());
        channel.writeInbound(request);

        FullHttpResponse response = channel.readOutbound();
        assertEquals(HttpResponseStatus.OK, response.status());
    }

    @Test
    @DisplayName("verify login fails, given credentials for a user that doesn't eixst, return http not found ")
    public void badLogin1() {
        UserDao userDao = new UserDao(dataSource);
        EmbeddedChannel channel = new EmbeddedChannel(new HttpRequestHandler("/websocket",userDao));

        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,
                HttpMethod.POST,
                loginUrl
        );
        request.content().writeCharSequence(MessageFactory.toJson(idmessage), CharsetUtil.UTF_8);
        HttpUtil.setContentLength(request, request.content().readableBytes());
        channel.writeInbound(request);

        FullHttpResponse response = channel.readOutbound();
        assertEquals(HttpResponseStatus.NOT_FOUND, response.status());
    }

    // TODO Not sure if I should add a test when the user name is correct and the passwrod is wrong

    @AfterEach
    public void cleanup() {
        try(Connection conn = dataSource.getConnection()) {
            try(Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE users");
            }
        }catch(SQLException e) {

        }
    }
}
