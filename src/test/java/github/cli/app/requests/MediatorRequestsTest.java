package github.cli.app.requests;

import github.cli.app.req.Mediator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag("Domain")
@ExtendWith(MockitoExtension.class)
public class MediatorRequestsTest {

    @Mock
    HttpClient client;

    @Test
    public void verifyHttpRequestMade() throws Exception{
        Mediator mediator = new Mediator(client);
        mediator.reqCommit("fakeId");
        verify(client, times(1)).sendAsync(any(), any());
    }

}
