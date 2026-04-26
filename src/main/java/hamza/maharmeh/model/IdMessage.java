package hamza.maharmeh.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.ObjectMapper;

public class IdMessage extends BaseMessage{

    @JsonCreator
    public IdMessage(@JsonProperty("sender") String sender) {
        super(sender);
    }

    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof IdMessage m)) return false;

        return m.sender().equals(this.sender());
    }
    public String toString() {
        return "Id message, Sender: " + this.sender();
    }
}
