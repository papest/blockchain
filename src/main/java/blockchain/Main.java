package blockchain;

public class Main {
    public static void main(String[] args) {
        int numberOfNulls = ConsoleHelper.askNumberOfNulls();

        ConsoleHelper.write(new MagicBlockchain(numberOfNulls)
                .generateBlock()
                .generateBlock()
                .generateBlock()
                .generateBlock()
                .generateBlock()
                .toString()

        );
    }
}

