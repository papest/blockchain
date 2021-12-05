package bclockchain;

import java.security.MessageDigest;
import java.util.*;

public class Blockchain {
    ArrayList<Block> blockchain = new ArrayList<>();

    public Blockchain() {
        blockchain.add(new Block());
    }

    public Blockchain generateBlock() {
        blockchain.add(new Block(blockchain.get(blockchain.size() - 1)));
        return this;
    }

    public boolean isValid() {
        String previousHash = blockchain.get(0).hash;

        for (int i = 1; i < blockchain.size(); ++i) {

            if (!blockchain.get(i).hashPreviousBlock.equals(previousHash)) {
                return false;
            }
            previousHash = blockchain.get(i).hash;
        }
        return true;

    }

    @Override
    public String toString() {
        return Arrays.toString(blockchain.toArray(new Block[0])).replaceAll("\\[|]", "")
                .replaceAll(", ", "\n");
    }
}

class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Block {
    final int id;
    final long timeStamp = new Date().getTime();
    final String hashPreviousBlock;
    final String hash;

    protected Block() {
        id = 1;
        hashPreviousBlock = "0";
        hash = StringUtil.applySha256(String.valueOf(id) + timeStamp + hashPreviousBlock);
    }

    protected Block(Block previous) {
        id = previous.id + 1;
        hashPreviousBlock = previous.hash;
        hash = StringUtil.applySha256(String.valueOf(id) + timeStamp + hashPreviousBlock);
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Id: " + id +
                "\nTimestamp: " + timeStamp +
                "\nHash of the previous block:\n" + hashPreviousBlock + "\n" +
                "Hash of the block:\n" + hash + "\n";
    }
}
