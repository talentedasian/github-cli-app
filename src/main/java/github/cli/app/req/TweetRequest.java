package github.cli.app.req;

public class TweetRequest extends HttpGetRequest {

    public TweetRequest(String tweetId) {
        super("tweets/%s", tweetId);
    }

}
