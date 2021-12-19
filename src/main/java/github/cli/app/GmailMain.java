package github.cli.app;

import github.Credentials;

public class GmailMain {

    public static void main(String[] args) {
        Credentials.load();
        var creds = Credentials.instance();

        
    }

}
