package blockchain;

import java.util.Date;
import java.util.Random;

public class MagicBlockchain extends Blockchain {
    final private int numberOfNulls;

    public MagicBlockchain(int numberOfNulls) {
        this.numberOfNulls = numberOfNulls;
    }


    class MagicBlock extends Block {
        Random random = new Random();
        int magic;
        long createTime;

        public MagicBlock(Block previous) {
            super(previous);
            long start = new Date().toInstant().getEpochSecond();
            this.hash = hash();
            this.createTime = new Date().toInstant().getEpochSecond() - start;
        }

        public MagicBlock() {
            super();
            long start = new Date().toInstant().getEpochSecond();
            this.hash = hash();
            this.createTime = new Date().toInstant().getEpochSecond() - start;
        }

        private String hash() {
            while (true) {
                this.magic = random.nextInt();
                String hash = StringUtil.applySha256(String.valueOf(id) + timeStamp + hashPreviousBlock + magic);
                if (hash.matches("0{" + numberOfNulls + "}.*")) {
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
