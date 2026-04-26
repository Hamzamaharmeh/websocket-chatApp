package hamza.maharmeh;

import hamza.maharmeh.model.BaseMessage;
import hamza.maharmeh.model.ChatMessage;
import hamza.maharmeh.model.IdMessage;
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
        assertThrows(MessageFactory.NoSuchMessageException.class,() -> MessageFactory.getMessage(input) );
    }

    private static Stream<Arguments> provideMessageAndClassType() {
        return Stream.of(
                Arguments.of(
                        "{\"sender\": \"hamza\",\"receiver\": \"yassen\",\"content\": \"Hello\",\"type\": \"chat\"}",
                        new ChatMessage("hamza","yassen","Hello")
                ),
                Arguments.of(
                        "{\"sender\": \"hamza\",\"type\": \"id\"}",
                        new IdMessage("hamza")
                )
        );
    }
}
