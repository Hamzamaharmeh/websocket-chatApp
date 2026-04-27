package hamza.maharmeh.model.outbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
public abstract class ErrorMessage {
    @JsonProperty
    private final String content;

    public ErrorMessage(String content) {
        this.content = content;
    }


}
