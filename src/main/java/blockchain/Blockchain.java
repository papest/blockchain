package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

class Blockchain implements Serializable {
    ArrayList<Block> blockchain = new ArrayList<>();

    public Blockchain() {
    }

    public Blockchain generateBlock() {
        if (blockchain.isEmpty()) {
            blockchain.add(new Block());
            return this;
        }
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
        return Arrays.toString(blockchain.toArray(new Block[0])).replaceAll("[\\[\\]]", "")
                .replaceAll(", ", "\n");
    }
}




class Block implements Serializable{
    final int id;
    final long timeStamp = new Date().getTime();
    String hashPreviousBlock;
    String hash;

    protected Block() {
        id = 1;
        hashPreviousBlock = "0";
        hash = hash();
    }

    private String hash() {
        return StringUtil.applySha256(String.valueOf(id) + timeStamp + hashPreviousBlock);
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