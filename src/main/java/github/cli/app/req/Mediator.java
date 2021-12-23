package github.cli.app.req;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import github.cli.app.commit.Tweet;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Mediator {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        var simpleMd = new SimpleModule();
        simpleMd.addDeserializer(Tweet.class, new Tweet.Deserializer());
        mapper.registerModule(simpleMd);
    }

    public static Tweet reqTweet(HandlerRequest handler) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpResponse<String> stringHttpResponse = handler.query().get();

        return mapper.readValue(stringHttpResponse.body(), Tweet.class);
    }


}
