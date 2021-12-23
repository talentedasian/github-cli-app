package github.cli.app.req;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface HandlerRequest {

    CompletableFuture<HttpResponse<String>> query() throws IOException, InterruptedException;

}
