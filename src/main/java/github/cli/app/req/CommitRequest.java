package github.cli.app.req;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class CommitRequest extends GithubHttpGetRequest {

    public CommitRequest(String commitId) {
        super("/repos/%s/%s/commits/%s", "talentedasian", "For-Politicians", commitId);
    }

}
