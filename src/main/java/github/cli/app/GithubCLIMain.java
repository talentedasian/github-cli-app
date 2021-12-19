package github.cli.app;

import github.Credentials;

public class GithubCLIMain {

    public static void main(String[] args) {
        Credentials.load();
        var creds = Credentials.instance();

        
    }

}
