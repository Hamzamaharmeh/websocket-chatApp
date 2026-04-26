package hamza.maharmeh.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tools.jackson.databind.ObjectMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatMessage.class, name = "chat"),
        @JsonSubTypes.Type(value = IdMessage.class, name = "id")
})
public abstract class BaseMessage {
    @JsonProperty
    private final String sender;
    public BaseMessage(String sender) {
        this.sender = sender;
    }

    public String sender() {
        return sender;
    }

}
