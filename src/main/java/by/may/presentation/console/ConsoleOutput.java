package by.may.presentation.console;

public final class ConsoleOutput {

    public static void success(String text) {
        System.out.println(Colors.GREEN + text + Colors.RESET);
    }

    public static void error(String text) {
        System.out.println(Colors.RED + text + Colors.RESET);
    }

    public static void info(String text) {
        System.out.println(Colors.YELLOW + text + Colors.RESET);
    }

    public static void title(String text) {
        System.out.println(Colors.BLUE + text + Colors.RESET);
    }

}
