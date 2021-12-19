package github.cli.app.requests;

import github.cli.app.commit.Commit;
import github.cli.app.req.Mediator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("Domain")
@ExtendWith(MockitoExtension.class)
public class MediatorRequestCommitTest {

    @Mock HttpClient client;
    @Mock HttpResponse response;

    @Test
    public void verifyHttpRequestMade() throws Exception{
        Mediator mediator = new Mediator(client);
        mediator.reqCommit("fakeId");
        verify(client, times(1)).sendAsync(any(), any());
    }

    @Test
    public void shouldReturnCommitInfoUponSuccessfulRequest() throws Exception{
        // although a fake, see documentation of github api (GET A COMMIT part) to know that this is enough for tests.
        String fakeResponse = """
                {
                  "url": "https://api.github.com/repos/octocat/Hello-World/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e",
                  "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e",
                  "commit": {
                     "message": "Fix all the bugs"
                  }
                }
                  """;

        when(response.body()).thenReturn(fakeResponse);
        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.supplyAsync(() -> response));

        Mediator mediator = new Mediator(client);

        assertThat(new Commit("6dcb09b5b57875f334f61aebed695e2e4193db5e", "Fix all the bugs"))
                .isEqualTo(mediator.reqCommit("fakeid"));
    }

}
