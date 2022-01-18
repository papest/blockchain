package blockchain;

public class Message {
    String username;
    String message;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s\n",username,message);
    }
}
