package github.cli.app.req;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import github.cli.app.exceptions.GenericTwitterResException;
import github.cli.app.tweet.Tweet;
import github.cli.app.user.User;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutionException;

public class Mediator {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        var simpleMd = new SimpleModule();
        simpleMd.addDeserializer(Tweet.class, new Tweet.Deserializer());
        simpleMd.addDeserializer(User.class, new User.Deserializer());
        mapper.registerModule(simpleMd);
    }

    public static Tweet reqTweet(HandlerRequest handler) throws ExecutionException, InterruptedException, IOException {
        return handleReq(handler, Tweet.class);
    }

    public static User reqUser(HandlerRequest handler) throws IOException, ExecutionException, InterruptedException {
        return handleReq(handler, User.class);
    }

    private static <T> T handleReq(HandlerRequest handler, Class classz) throws IOException, InterruptedException, ExecutionException {
        return (T) handler.query().thenApply(res -> {
            try {
                return mapper.readValue(res.body(), classz);
            } catch (JsonProcessingException e) {
                throw new GenericTwitterResException();
            }
        }).get();
    }

}
