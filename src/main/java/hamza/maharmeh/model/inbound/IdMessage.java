package hamza.maharmeh.model.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdMessage extends BaseMessage{

    @JsonProperty
    private final String password;
    @JsonCreator
    public IdMessage(@JsonProperty("sender") String sender,@JsonProperty("password") String password) {
        super(sender);
        this.password = password;
    }

    public String password() {
        return this.password;
    }
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof IdMessage m)) return false;

        return m.sender().equals(this.sender()) && m.password().equals(this.password());
    }
    public String toString() {
        return "Id message, Sender: " + this.sender();
    }
}
