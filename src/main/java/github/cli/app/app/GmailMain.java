package gmail.cli.app.app;

import gmail.cli.app.Credentials;

public class GmailMain {

    public static void main(String[] args) {
        Credentials.load();
        var creds = Credentials.instance();

        
    }

}
