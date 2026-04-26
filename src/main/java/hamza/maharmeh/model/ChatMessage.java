package hamza.maharmeh.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.ObjectMapper;

public class ChatMessage extends BaseMessage {
    @JsonProperty
    private final String content;
    @JsonProperty
    private final String receiver;
    @JsonCreator
    public ChatMessage(@JsonProperty("sender")String sender,
                       @JsonProperty("receiver") String receiver,
                       @JsonProperty("content") String content) {

        super(sender);
        this.content = content;
        this.receiver = receiver;
    }

    public String content() {
        return content;
    }
    public String receiver() {
        return receiver;
    }

    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof ChatMessage c)) return false;

        return c.sender().equals(this.sender())
                && c.receiver.equals(this.receiver)
                && c.content.equals(this.content);
    }

    public String toString() {
        return "Chat message from " + this.sender() + " to " + this.receiver;
    }
}
