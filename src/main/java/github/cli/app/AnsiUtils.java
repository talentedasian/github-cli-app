package github.cli.app;

public class AnsiUtils {

    private AnsiUtils() {
        throw new UnsupportedOperationException();
    }

    public static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void reset() {
        System.out.print("\033[0m");
        System.out.flush();
    }

}
