package blockchain;

import java.util.ArrayList;

public class ChatBlockChain extends MinerBlockchain {

    ArrayList<Message> newMessages = new ArrayList<>();
    ArrayList<Message> messages = new ArrayList<>();

    class ChatBlock extends MinersBlock {
        ArrayList<Message> data = new ArrayList<>();

        public ChatBlock() {
        }

        public ChatBlock(Block block) {
            super(block);
            data = messages;
        }


        @Override
        public String toString() {
            return "Block:\n" +
                    "Created by miner # " + minerNumber +
                    "\nId: " + id +
                    "\nTimestamp: " + timeStamp +
                    "\nMagic number: " + magic +
                    "\nHash of the previous block:\n" + hashPreviousBlock +
                    "\nHash of the block:\n" + hash +
                    "\nBlock data: " + (data.isEmpty() ? "no messages" : data.toString().replaceAll("[\\[\\]]", "")
                    .replaceAll(", ", "\n")) +
                    "\nBlock was generating for " + createTime + " seconds\n" +
                    ((nextNumberOfNulls > numberOfNulls) ? "N was increased to " + nextNumberOfNulls :
                            (nextNumberOfNulls == numberOfNulls) ? "N stays the same" : "N was decreased by  1") + "\n";

        }
    }

    ChatBlock generateBlock(int n) {

        if (blockchain.isEmpty()) {
            return new ChatBlock();
        } else {
            return new ChatBlock(blockchain.get(blockchain.size() - 1));
        }
    }

    @Override
    public boolean accept(MinersBlock block) {
        if (super.accept(block)) {
            messages = newMessages;
            newMessages = new ArrayList<>();
            return true;
        }

        return false;

    }


}
