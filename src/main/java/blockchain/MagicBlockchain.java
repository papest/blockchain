package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class MagicBlockchain extends Blockchain implements Serializable {
    int numberOfNulls;

    public MagicBlockchain() {
        this.numberOfNulls = 0;
    }

    public MagicBlockchain(int numberOfNulls) {
        this.numberOfNulls = numberOfNulls;
    }


    class MagicBlock extends Block implements Serializable{

        int magic;
        long createTime;

        public MagicBlock(Block previous) {
            super(previous);

        }

        public MagicBlock() {
            super();


        }

        @Override
        String hash() {
            long start = new Date().toInstant().getEpochSecond();
            while (true) {
                this.magic = ThreadLocalRandom.current().nextInt();

                String hash = StringUtil.applySha256(String.valueOf(id) + timeStamp + hashPreviousBlock + magic);
                if (hash.matches("0{" + numberOfNulls + "}.*")) {
                    this.createTime = new Date().toInstant().getEpochSecond() - start;
                    return hash;

                }
            }
        }

        @Override
        public String toString() {
            return "Block:\n" +
                    "Id: " + id +
                    "\nTimestamp: " + timeStamp +
                    "\nMagic number: " + magic +
                    "\nHash of the previous block:\n" + hashPreviousBlock + "\n" +
                    "Hash of the block:\n" + hash + "\n" +
                    "Block was generating for " + createTime + " seconds\n";

        }
    }

    @Override
    public MagicBlockchain generateBlock() {
        if (blockchain.isEmpty()) {
            blockchain.add(new MagicBlock());
            return this;
        }
        blockchain.add(new MagicBlock(blockchain.get(blockchain.size() - 1)));
        return this;
    }


}
