package github.cli.app.req;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import github.cli.app.commit.Commit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Mediator {

    private final HttpClient httpClient;
    private ObjectMapper mapper;

    public Mediator(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient,
                "The HttpClient cannot be null. Providing the default is enough though");
        this.mapper = new ObjectMapper();
        SimpleModule md = new SimpleModule();
        md.addDeserializer(Commit.class, new Commit.Deserializer());
        mapper.registerModule(md);
    }

    public Mediator(HttpClient httpClient, ObjectMapper mapper) {
        this.httpClient = httpClient == null ? HttpClient.newBuilder().build() : httpClient;
        this.mapper = mapper;
        SimpleModule md = new SimpleModule();
        md.addDeserializer(Commit.class, new Commit.Deserializer());
        mapper.registerModule(md);
    }

    public Commit reqCommit(String commitId) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest req = new CommitRequest(commitId);
        HttpResponse<String> stringHttpResponse = httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).get();

        return mapper.readValue(stringHttpResponse.body(), Commit.class);
    }

}
