package blockchain;

import java.util.Scanner;

public class ConsoleHelper {
    static Scanner scanner = new Scanner(System.in);

    public static void write(String s) {

        System.out.print(s);
    }

    private static String readString() {
        return scanner.nextLine();
    }

    public static int askNumberOfNulls() {
        while (true) {
            try {
                write("Enter how many zeros the hash must start with: ");
                return Integer.parseInt(readString().trim());
            } catch (NumberFormatException e) {
            }
        }
    }
}