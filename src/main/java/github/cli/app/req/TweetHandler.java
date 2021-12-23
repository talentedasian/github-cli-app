package github.cli.app.req;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class TweetHandler implements HandlerRequest {

    private final HttpClient httpClient;
    private final String tweetId;

    public TweetHandler(HttpClient httpClient, String tweetId) {
        this.httpClient = httpClient;
        this.tweetId = tweetId;
    }

    @Override
    public CompletableFuture<HttpResponse<String>> query() {
        return httpClient.sendAsync(new TweetRequest(tweetId), ofString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TweetHandler that = (TweetHandler) o;

        if (!httpClient.equals(that.httpClient)) return false;
        return tweetId.equals(that.tweetId);
    }

    @Override
    public int hashCode() {
        int result = httpClient.hashCode();
        result = 31 * result + tweetId.hashCode();
        return result;
    }
}
