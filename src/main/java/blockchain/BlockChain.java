package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

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

        return blockchain.stream().map(Block::toString).collect(Collectors.joining("\n"));
    }
}


class Block implements MinerBlockchain.InterfaceBlock, Serializable {
    int id;
    long timeStamp = new Date().getTime();
    String hashPreviousBlock;
    String hash;


    protected Block() {
        id = 1;
        hashPreviousBlock = "0";
        hash = hash();
    }

    String hash() {
        return StringUtil.applySha256(String.valueOf(id) + timeStamp + hashPreviousBlock);
    }

    protected Block(Block previous) {
        id = previous.id + 1;
        hashPreviousBlock = previous.hash;
        hash = hash();
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