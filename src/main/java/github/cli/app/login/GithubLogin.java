package github.cli.app.login;

import github.Credentials;
import github.cli.app.req.GithubHttpGetRequest;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GithubLogin {

    public static final Path LOGIN_PATH = Paths.get("login.txt");

    private static String token = null;
    private GithubLogin() {
        throw new UnsupportedOperationException();
    }

    public static String token() throws IOException {
        if (token == null) {
            File loginFile = LOGIN_PATH.toFile();
            if (loginFile.exists()) {
                String tokenFromFile = new BufferedReader(new FileReader(loginFile)).readLine();
                if (tokenFromFile.isEmpty() || tokenFromFile.isBlank()) throw new IllegalStateException("No user is currently logged in");
                GithubLogin.token = tokenFromFile;
                return GithubLogin.token;
            }

            throw new IllegalStateException("No user is currently logged in");
        }

        return token;
    }

    public static void init(String token) throws IOException {
        File file = LOGIN_PATH.toFile();
        if (file.createNewFile()) {
            FileWriter stream = new FileWriter(file);
            stream.write(token);
            stream.close();
        } else {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(token);
            fileWriter.close();
        }
    }

    public static void unInit() throws IOException {
        token = null;
        File file = LOGIN_PATH.toFile();
        file.delete();

    }

    public static boolean isLoggedIn(HttpClient client) throws ExecutionException, InterruptedException {
        File file = LOGIN_PATH.toFile();
        if (file.exists()) {
            String clientId = Credentials.instance().clientKey();
            String baseUri = GithubHttpGetRequest.BASE_URI;
            HttpRequest req = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Authorization", basicAuth())
                    .uri(URI.create(baseUri.concat("/applications/%s/token".formatted(clientId))))
                    .build();
            return client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(res -> res.statusCode() == 200 ? true : false)
                    .get();
        }

        return false;
    }

    private static String basicAuth() {
        Credentials creds = Credentials.instance();
        String username = creds.clientKey();
        String password = creds.secretKey();

        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
