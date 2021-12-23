package github.cli.app.exceptions;

public class GenericTwitterResException extends RuntimeException {

    public GenericTwitterResException() {
        super("Invalid twitter response. Twitter might have changed the response?");
    }
}
