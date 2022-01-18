package blockchain;

import java.io.Serializable;

public class Message implements Serializable {
    byte[] signature;
    String username;
    String message;
    int messageNumber;

    public Message(String username, String message, byte[] signature) {
        this.username = username;
        this.message = message;
        this.messageNumber = ChatBlockChain.getLastMessageNumber() + 1;
        this.signature = signature;


    }

    @Override
    public String toString() {
        return String.format("%s: %s\n", username, message);
    }
}
