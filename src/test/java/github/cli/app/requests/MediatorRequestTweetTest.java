package github.cli.app.requests;

import github.cli.app.req.TweetHandler;
import github.cli.app.req.Mediator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("Domain")
@ExtendWith(MockitoExtension.class)
public class MediatorRequestTweetTest {

    @Mock HttpClient client;
    @Mock HttpResponse response;

    // although a fake, see documentation of github api (GET A COMMIT part) to know that this is enough for tests.
    private String fakeResponse = """
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
    public void verifyHttpRequestMade() throws Exception{

        when(response.body()).thenReturn(fakeResponse);
        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.supplyAsync(() -> response));

        TweetHandler handler = new TweetHandler(client, "fakeId");
        Mediator.reqTweet(handler);

        verify(client, times(1)).sendAsync(any(), any());
    }

}
