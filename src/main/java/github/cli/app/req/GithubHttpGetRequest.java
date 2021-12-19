package github.cli.app.req;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public abstract class GithubHttpGetRequest extends HttpRequest {

    private final String BASE_URI = "https://api.github.com/";

    private final String uri;

    private final HttpRequest.Builder reqBuilder;

    public GithubHttpGetRequest(String uri, String... queryParams) {
        this.uri = uri.formatted(queryParams);

        reqBuilder = HttpRequest.newBuilder(URI.create(BASE_URI.concat(this.uri)))
                .expectContinue(true)
                .version(HttpClient.Version.HTTP_1_1);
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        return Optional.empty();
    }

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public Optional<Duration> timeout() {
        return Optional.empty();
    }

    @Override
    public boolean expectContinue() {
        return true;
    }

    @Override
    public URI uri() {
        return reqBuilder.build().uri();
    }

    @Override
    public Optional<HttpClient.Version> version() {
        return Optional.of(HttpClient.Version.HTTP_1_1);
    }

    @Override
    public HttpHeaders headers() {
        return reqBuilder.build().headers();
    }
}
