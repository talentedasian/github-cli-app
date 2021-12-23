package github.cli.app.login;

import github.Credentials;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Domain")
public class LoginTest {

    Path CREDS_PATH = Paths.get("../../creds.json");

    @AfterEach
    public void teardown() throws IOException {
        Login.unInit();
    }

    Path LOGIN_PATH = Login.LOGIN_PATH;

    @Test
    public void validateContents() throws Exception{
        String access_token = "access_token";
        Login.init(access_token);

        File loginFile = LOGIN_PATH.toFile();

        BufferedReader stream = new BufferedReader(new FileReader(loginFile));
        
        assertThat(stream.readLine())
                .isEqualTo(access_token);
    }

    @Test
    public void initOverwritesPreviousInits() throws Exception{
        String access_token_prior = "access_token_before";
        String access_token_predated = "access_token_after";

        Login.init(access_token_prior);
        Login.init(access_token_predated);

        assertThat(Login.token())
                .isEqualTo(access_token_predated);
    }

    @Test
    public void noLoggedInUserThrowsIllegalStateException() throws Exception{
        ThrowingCallable notLoggedIn = () -> Login.token();

        assertThatThrownBy(notLoggedIn)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No user is currently logged in");
    }

    @Test
    public void notLoggedInIfFileIsMissing() throws Exception{
        Credentials.load(CREDS_PATH);
        boolean isLoggedIn = Login.isLoggedIn(HttpClient.newHttpClient());

        assertThat(isLoggedIn)
                .isFalse();
    }

    @Test
    public void non200HttpReturnShouldNotBeLoggedIn() throws Exception{
        Credentials.load(CREDS_PATH);
        Login.init("fakeInit");

        HttpClient client = mock(HttpClient.class);
        HttpResponse res = new FakeResponse(400);

        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.supplyAsync(() -> res));

        boolean isLoggedIn = Login.isLoggedIn(client);

        assertThat(isLoggedIn)
                .isFalse();
    }

    @Test
    public void ok200HttpReturnShouldNotBeLoggedIn() throws Exception{
        Credentials.load(CREDS_PATH);
        Login.init("fakeInit");

        HttpClient client = mock(HttpClient.class);
        HttpResponse res = new FakeResponse(200);

        when(client.sendAsync(any(), any())).thenReturn(CompletableFuture.supplyAsync(() -> res));

        boolean isLoggedIn = Login.isLoggedIn(client);

        assertThat(isLoggedIn)
                .isTrue();
    }

    static class FakeResponse extends AbstractFakeHttpResponse {
        private final int statusCode;
        public FakeResponse(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public int statusCode() {
            return statusCode;
        }
    }

}
