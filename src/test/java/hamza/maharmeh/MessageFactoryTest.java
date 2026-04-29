package hamza.maharmeh;

import hamza.maharmeh.exceptions.NoSuchMessageException;
import hamza.maharmeh.model.inbound.BaseMessage;
import hamza.maharmeh.model.inbound.ChatMessage;
import hamza.maharmeh.model.inbound.IdMessage;
import hamza.maharmeh.model.MessageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageFactoryTest {

    @ParameterizedTest
    @MethodSource("provideMessageAndClassType")
    @DisplayName("Verify Message creation, When given a correct message json , returns message object")
    public void createTest(String input, BaseMessage expected) {
        BaseMessage inputMessage = MessageFactory.getMessage(input);
        assertEquals(expected, inputMessage, "expected " + expected.toString() + " but got " + inputMessage.toString());
    }

    @ParameterizedTest
    @DisplayName("Verify message creation, when given incorrect messages, throws NoSuchMessageException")
    @ValueSource(strings = {
           "{\"sendar\": \"hamza\",\"receiver\": \"yassen\",\"content\": \"hello\"}",
            "{\"sender\": \"hamza\",\"receiver\": \"yassen\"}",
            "{}",
            "{\"randomKey\": 123}",
    })
    public void failTest(String input) {
        assertThrows(NoSuchMessageException.class,() -> MessageFactory.getMessage(input) );
    }

    @Test
    @DisplayName("Verify message to json conversion, Given a message object, return valid json")
    public void toJson() {
        IdMessage message = new IdMessage("hamza","123");
        String result = MessageFactory.toJson(message);
        assertEquals(new IdMessage("hamza","123"), MessageFactory.getMessage(result));
    }
    private static Stream<Arguments> provideMessageAndClassType() {
        return Stream.of(
                Arguments.of(
                        "{\"sender\": \"hamza\",\"receiver\": \"yassen\",\"content\": \"Hello\",\"type\": \"chat\"}",
                        new ChatMessage("hamza","yassen","Hello")
                ),
                Arguments.of(
                        "{\"sender\": \"hamza\",\"password\": \"123\",\"type\": \"id\"}",
                        new IdMessage("hamza","123")
                )
        );
    }
}
