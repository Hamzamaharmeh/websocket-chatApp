package hamza.maharmeh.model;

import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public class MessageFactory {

    public static class NoSuchMessageException extends RuntimeException {
    }
    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .build();
    public static BaseMessage getMessage(String input) {
        try {
            return mapper.readValue(input, BaseMessage.class);
        }catch (Exception e){
            throw new  NoSuchMessageException();
        }
    }
}
