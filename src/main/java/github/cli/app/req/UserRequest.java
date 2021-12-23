package github.cli.app.req;

public class UserRequest extends HttpGetRequest{
    public UserRequest(String userId) {
        super("/users/%s", userId);
    }
}
