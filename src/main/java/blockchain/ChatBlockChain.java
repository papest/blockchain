package blockchain;

import java.security.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ChatBlockChain extends MinerBlockchain {
    static final AtomicInteger lastMessageNumber = new AtomicInteger(0);

    ArrayList<Message> newMessages = new ArrayList<>();
    ArrayList<Message> messages = new ArrayList<>();
    Map<Miner, PublicKey> publicKeys = new HashMap<>();

    public static int getLastMessageNumber() {
        synchronized (lastMessageNumber) {
            return lastMessageNumber.get();
        }
    }

    public boolean receive(Message message, Miner sender) {
        synchronized (lastMessageNumber) {
            if (checkNumberMessage(message) && verifiedSignature(message, sender)) {
                lastMessageNumber.set(message.messageNumber);
                newMessages.add(message);

                return true;
            }
            return false;
        }
    }

    class ChatBlock extends MinersBlock {
        ArrayList<Message> data;

        public ChatBlock() {
        }

        public ChatBlock(Block block) {
            super(block);

        }

        @Override
        String hash() {
            this.data = blockchain.isEmpty() ? new ArrayList<>() : messages;
            long start = new Date().toInstant().getEpochSecond();
            while (true) {
                this.magic = ThreadLocalRandom.current().nextInt();
                String hash = StringUtil.applySha256(String.valueOf(id) + minerNumber + timeStamp
                        + hashPreviousBlock + magic + data.stream().map(a -> a.username + a.message)
                        .collect(Collectors.joining()));
                if (hash.matches("0{" + String.valueOf(ChatBlockChain.this.numberOfNulls) + "}.*")) {
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
                    "\nHash of the previous block:\n" + hashPreviousBlock +
                    "\nHash of the block:\n" + hash +
                    "\nBlock data: " + (data.isEmpty() ? "no messages\n" : "\n"
                    + data.stream().map(Message::toString).collect(Collectors.joining()))
                    +
                    "Block was generating for " + createTime + " seconds\n" +
                    ((nextNumberOfNulls > numberOfNulls) ? "N was increased to " + nextNumberOfNulls :
                            (nextNumberOfNulls == numberOfNulls) ? "N stays the same" : "N was decreased by  1") + "\n";

        }
    }

    private boolean checkNumberMessage(Message message) {
        return message.messageNumber > lastMessageNumber.get();
    }

    private boolean verifiedSignature(Message message, Miner sender) {
        if (!publicKeys.containsKey(sender)) {
            publicKeys.put(sender, sender.getPublicKey());
        }
        Signature sig = null;
        try {
            sig = Signature.getInstance("SHA1withRSA");

            sig.initVerify(publicKeys.get(sender));
            sig.update(message.message.getBytes());


            return sig.verify(message.signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;


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
