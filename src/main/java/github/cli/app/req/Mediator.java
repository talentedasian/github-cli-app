package github.cli.app.req;

import github.cli.app.commit.Commit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Mediator {

    private final HttpClient httpClient;

    public Mediator(HttpClient httpClient) {
        this.httpClient = httpClient == null ? HttpClient.newBuilder().build() : httpClient;
    }

    public Commit reqCommit(String commitId) {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:8080/")).build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        return new Commit(commitId);
    }

}
