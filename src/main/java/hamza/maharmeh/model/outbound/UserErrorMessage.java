package hamza.maharmeh.model.outbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserErrorMessage extends ErrorMessage {

    @JsonCreator
    public UserErrorMessage(@JsonProperty("content")String content) {
        super(content);
    }
}
