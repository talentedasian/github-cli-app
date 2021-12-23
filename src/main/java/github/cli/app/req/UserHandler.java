package github.cli.app.req;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UserHandler implements HandlerRequest {

    private final HttpClient httpClient;
    private final String userId;

    public UserHandler(HttpClient client, String userId) {
        this.httpClient = client;
        this.userId = userId;
    }

    @Override
    public CompletableFuture<HttpResponse<String>> query() throws IOException, InterruptedException {
        return httpClient.sendAsync(new UserRequest(userId), HttpResponse.BodyHandlers.ofString());
    }
}
