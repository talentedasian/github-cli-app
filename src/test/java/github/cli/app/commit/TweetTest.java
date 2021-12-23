package github.cli.app.commit;

import github.cli.app.login.AbstractFakeHttpResponse;
import github.cli.app.req.Mediator;
import github.cli.app.req.TweetHandler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Domain")
public class TweetTest {

    String fakeResponse = """
            {
              "data": {
                "author_id": "2244994945",
                "created_at": "2018-11-26T16:37:10.000Z",
                "text": "Just getting started with Twitter APIs? Find out what you need in order to build an app. Watch this video! https://t.co/Hg8nkfoizN",
                "id": "1067094924124872705"
              },
              "includes": {
                "users": [
                  {
                    "verified": true,
                    "username": "TwitterDev",
                    "id": "2244994945",
                    "name": "Twitter Dev"
                  }
                ]
              }
            }
            """;

    @Test
    public void testDeserializer() throws Exception{
        HttpClient client = mock(HttpClient.class);
        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.supplyAsync(() -> new FakeHttpRes(fakeResponse)));

        Tweet tweet = Mediator.reqTweet(new TweetHandler(client, "fakeId"));

        assertThat(tweet)
                .isEqualTo(new Tweet(tweet.id(), tweet.message(), tweet.author()));
    }

    static class FakeHttpRes extends AbstractFakeHttpResponse {

        String body;

        public FakeHttpRes(String body) {
            this.body = body;
        }

        @Override
        public Object body() {
            return body;
        }
    }

}
