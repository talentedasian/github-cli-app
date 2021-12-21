package github.cli.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.codeshelf.consoleui.elements.ConfirmChoice;
import de.codeshelf.consoleui.prompt.ConfirmResult;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import github.Credentials;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class GithubCLIMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        Credentials.load();
        var creds = Credentials.instance();
        AnsiConsole.systemInstall();
        ConsolePrompt prompt = new ConsolePrompt();

//        githubAuth(creds, prompt);
    }

    private static void githubAuth(Credentials creds, ConsolePrompt prompt) throws IOException, InterruptedException {
        AnsiUtils.clearTerminal();
        String clientKey = creds.clientKey();
        String uri = "https://github.com/login/device/code?client_id=%s&scope=repo,user".formatted(clientKey);
        String tokUri = "https://github.com/login/oauth/access_token?client_id=%s&" +
                "device_code=%s&grant_type=urn:ietf:params:oauth:grant-type:device_code";
        HttpClient cl = HttpClient.newBuilder().build();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest post = HttpRequest.newBuilder(URI.create(uri)).POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json").build();
        HttpResponse<String> send = cl.send(post, HttpResponse.BodyHandlers.ofString());

        String verificationUri = mapper.readTree(send.body()).get("verification_uri").asText();
        String deviceCode = mapper.readTree(send.body()).get("device_code").asText();
        String userCode = mapper.readTree(send.body()).get("user_code").asText();

        String boldGithub = Ansi.ansi().bold().a("github ").reset().toString();

        System.out.println("To use this app, you need to login first\n");
        HashMap<String, ? extends PromtResultItemIF> promptMap = promptUser(prompt, verificationUri, userCode, boldGithub);

        boolean didUserPressYes = ((ConfirmResult) promptMap.get("login")).getConfirmed().equals(ConfirmChoice.ConfirmationValue.YES);
        if (!didUserPressYes) {
            AnsiUtils.clearTerminal();
            System.out.println("\033[31mMust login to use this app\n");
            System.out.println("\033[32mLast chance\n");

            System.out.println("\033[39m");
            HashMap<String, ? extends PromtResultItemIF> ptMap = promptUser(prompt, verificationUri, userCode, boldGithub);
            if (!didUserPressYes) AnsiUtils.clearTerminal(); System.exit(0);

            openChrome(verificationUri);

            takeAccessCode(clientKey, tokUri, cl, mapper, deviceCode);
        }

        openChrome(verificationUri);

        takeAccessCode(clientKey, tokUri, cl, mapper, deviceCode);
    }

    private static void takeAccessCode(String clientKey, String tokUri, HttpClient cl, ObjectMapper mapper, String deviceCode) throws InterruptedException, IOException {
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            // must follow github api rate limit
            Thread.sleep(6000);

            HttpRequest token = HttpRequest.newBuilder(URI.create(tokUri.formatted(clientKey, deviceCode)))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Accept", "application/json").build();

            HttpResponse<String> accessTokenRes = cl.send(token, HttpResponse.BodyHandlers.ofString());
            String body = accessTokenRes.body();
            JsonNode error = mapper.readTree(body).get("error");
            if (error == null) {
                isLoggedIn = true;
            } else {
                if (error.asText().equals("expired_token")) {
                    AnsiUtils.clearTerminal();
                    System.out.println("\033[31mToo slow, my oauth parameters have expired because of you");
                    AnsiUtils.reset();

                    System.exit(0);
                } else if (error.asText().equals("access_denied")) {
                    AnsiUtils.clearTerminal();
                    System.out.println("\033[31mYou know you can't use the app if you cancelled it right?");
                    AnsiUtils.reset();

                    System.exit(0);
                }
            }
        }
        AnsiUtils.clearTerminal();
    }

    private static void openChrome(String verificationUri) throws IOException {
        String openChromeCommand = "./open-chrome.sh".formatted(verificationUri);
        ProcessBuilder processBuilder = new ProcessBuilder(openChromeCommand);
        processBuilder.start();
    }

    private static HashMap<String, ? extends PromtResultItemIF> promptUser(ConsolePrompt prompt, String verificationUri, String userCode, String boldGithub) throws IOException {
        PromptBuilder promptBuilder = prompt.getPromptBuilder().createConfirmPromp()
                .name("login")
                .message("Enter this code %s on %s s"
                        .formatted(userCode, boldGithub, verificationUri))
                .addPrompt();
        HashMap<String, ? extends PromtResultItemIF> promptMap = prompt.prompt(promptBuilder.build());
        return promptMap;
    }

}