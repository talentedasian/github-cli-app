package github.cli.app.login;

import github.cli.app.req.Mediator;
import github.cli.app.req.UserHandler;
import github.cli.app.user.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Domain")
public class UserTest {

    String fakeResponse = """
            {
              "data": {
                "id": "2244994945",
                "name": "Twitter Dev",
                "username": "TwitterDev"
              }
            }
            """;

    @Test
    public void testDeserializer() throws Exception{
        HttpClient client = mock(HttpClient.class);
        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.supplyAsync(() -> new FakeHttpRes(fakeResponse)));

        User user = Mediator.reqUser(new UserHandler(client, "fakeId"));

        assertThat(user)
                .isEqualTo(new User(user.id()));
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
