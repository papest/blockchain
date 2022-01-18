package blockchain;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class MinerBlockchain extends MagicBlockchain {
    final int MINIMUM_CREATE_TIME = 9;
    final int MAXIMUM_CREATE_TIME = 60;

    public MinerBlockchain() {

    }

    public int nextNumberOfNulls(MinersBlock block) {
        return block.createTime < MINIMUM_CREATE_TIME ? block.numberOfNulls + 1
                : block.createTime > MAXIMUM_CREATE_TIME ? Math.max(block.numberOfNulls - 1, 0)
                : block.numberOfNulls;
    }

    public boolean accept(MinersBlock block) {
        if (blockchain.size() == 0) {
            if (block.hashPreviousBlock.equals("0")) {
                blockchain.add(block);
                numberOfNulls = block.nextNumberOfNulls;
                return true;
            }
            return false;
        }
        if (block.numberOfNulls == ((MinersBlock) blockchain.get(blockchain.size() - 1)).nextNumberOfNulls
                && block.hashPreviousBlock.equals(blockchain.get(blockchain.size() - 1).hash)) {
            blockchain.add(block);
            numberOfNulls = block.nextNumberOfNulls;
            return true;
        }
        return false;
    }

    class MinersBlock extends MagicBlock {
        int minerNumber;
        int numberOfNulls = MinerBlockchain.this.numberOfNulls;
        int nextNumberOfNulls;

        MinersBlock() {
            super();
            nextNumberOfNulls = nextNumberOfNulls(this);
        }

        public MinersBlock(Block block) {
            super(block);
            nextNumberOfNulls = nextNumberOfNulls(this);
        }

        @Override
        String hash() {
            long start = new Date().toInstant().getEpochSecond();
            while (true) {
                this.magic = ThreadLocalRandom.current().nextInt();
                String hash = StringUtil.applySha256(String.valueOf(id) + minerNumber + timeStamp
                        + hashPreviousBlock + magic);
                if (hash.matches("0{" + String.valueOf(MinerBlockchain.this.numberOfNulls) + "}.*")) {
                    this.createTime = new Date().toInstant().getEpochSecond() - start;
                    return hash;

                }
            }
        }

        @Override
        public String toString() {
            return "Block:\n" +
                    "Created by miner # " + minerNumber +
                    "\nId: " + id +
                    "\nTimestamp: " + timeStamp +
                    "\nMagic number: " + magic +
                    "\nHash of the previous block:\n" + hashPreviousBlock + "\n" +
                    "Hash of the block:\n" + hash + "\n" +
                    "Block was generating for " + createTime + " seconds\n" +
                    ((nextNumberOfNulls > numberOfNulls) ? "N was increased to " + nextNumberOfNulls :
                            (nextNumberOfNulls == numberOfNulls) ? "N stays the same" : "N was decreased by  1") + "\n";

        }
    }

}
