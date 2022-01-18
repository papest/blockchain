package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class VCBlockChain extends ChatBlockChain implements Serializable {
    public boolean receive(Transaction transaction, Miner<VCBlock> sender) {
        return  validTransaction(transaction) && super.receive(transaction, sender);

    }

    private boolean validTransaction(Transaction transaction) {
        return transaction.sum >=0 && sumVC(transaction.sender) >= transaction.sum;
    }

    public class VCBlock extends ChatBlock implements Serializable {
        final int COST_OF_CREATION = 100;
        public Miner<VCBlock> miner;

        protected VCBlock() {
        }
        protected VCBlock(Block block) {
            super(block);
        }
        @Override
        String hash() {
            this.data = blockchain.isEmpty() ? new ArrayList<>() : messages;
            long start = new Date().toInstant().getEpochSecond();
            while (true) {
                this.magic = ThreadLocalRandom.current().nextInt();
                String hash = StringUtil.applySha256(String.valueOf(id) + minerNumber + timeStamp
                        + hashPreviousBlock + magic + data.stream().map(a -> (Transaction) a).map(Transaction::toString)
                        .collect(Collectors.joining()));
                if (hash.matches("0{" + String.valueOf(VCBlockChain.this.numberOfNulls) + "}.*")) {
                    this.createTime = new Date().toInstant().getEpochSecond() - start;
                    return hash;

                }
            }
        }

        @Override
        public String toString() {
            return "Block:\n" +
                    "Created by miner # " + minerNumber +
                    String.format("\nMiner#%s gets %s VC", minerNumber, COST_OF_CREATION) +
                    "\nId: " + id +
                    "\nTimestamp: " + timeStamp +
                    "\nMagic number: " + magic +
                    "\nHash of the previous block:\n" + hashPreviousBlock +
                    "\nHash of the block:\n" + hash +
                    "\nBlock data: \n" + (data.isEmpty() ? "No transactions\n" :
                    data.stream().map(Message::toString).collect(Collectors.joining()))
                    +
                    "Block was generating for " + createTime + " seconds\n" +
                    ((nextNumberOfNulls > numberOfNulls) ? "N was increased to " + nextNumberOfNulls :
                            (nextNumberOfNulls == numberOfNulls) ? "N stays the same" : "N was decreased by  1") + "\n";

        }
    }
    public int sumVC(Miner<VCBlock> miner) {
        int sendTakeMoney = this.blockchain.stream()
                .flatMap(block -> ((VCBlock) block).data.stream()).flatMapToInt(transaction -> {
                    int sumTake = 0;
                    int sumSend = 0;
                    if (((Transaction) transaction).recipient.equals(miner)) {
                        sumTake = ((Transaction) transaction).sum;
                    }
                    if (((Transaction) transaction).sender.equals(miner)) {
                        sumTake =  - ((Transaction) transaction).sum;
                    }
                    return IntStream.of(sumSend, sumTake);}).sum();
        int getMoney = this.blockchain.stream()
                .filter(block ->  ((VCBlock) block).miner == miner)
                .mapToInt(a -> ((VCBlock) a).COST_OF_CREATION).sum();
        return sendTakeMoney + getMoney + miner.INITIAL_SUM;
    }

    boolean validTransactions(VCBlock vcBlock) {
//        Map<Miner<VCBlock>, Integer> sendMoney = Stream.of(this.blockchain.stream()
//                                .flatMap(block -> ((VCBlock) block).data.stream()),
//                vcBlock.data.stream()).map(transaction -> (Transaction) transaction)
//                .collect(Collectors.groupingBy(a -> a.sender,
//                Collectors.summingInt(b -> - b.getSum())));
//        Map<Miner<VCBlock>, Integer> getMoney = Stream.of(this.blockchain.stream(), Stream.of(vcBlock))
//                .collect(Collectors.groupingBy(a -> ((VCBlock) a).miner,
//                        Collectors.summingInt(b -> ((VCBlock) b).COST_OF_CREATION)));
//        Map<Miner<VCBlock>, Integer> takeMoney = Stream.of(this.blockchain.stream().flatMap(block -> ((VCBlock) block).data.stream()),
//                vcBlock.data.stream()).collect(Collectors.groupingBy(a -> ((Transaction) a).recipient,
//                Collectors.summingInt(b -> ((Transaction) b).getSum())));
//
//        Map<Miner<VCBlock>, Integer> sumOfTransactions =
//                Stream.of(sendMoney.entrySet().stream(), getMoney.entrySet().stream(), takeMoney.entrySet().stream())
//                        .flatMap(z -> z)
//                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
//        return sumOfTransactions.entrySet().stream()
//                .noneMatch(minerEntry -> minerEntry.getValue() + minerEntry.getKey().INITIAL_SUM <= 0);
        Map<Miner<VCBlock>, Integer> money = new HashMap<>();
vcBlock.data.stream().map(transaction -> (Transaction) transaction)
        .peek(transaction -> {
           money.merge(transaction.sender, - transaction.sum, Integer::sum);
           money.merge(transaction.recipient, transaction.sum, Integer::sum);
        });
      return money.entrySet().stream()
              .noneMatch(a -> sumVC(a.getKey()) + a.getValue() < 0);

    }

    @Override
    public boolean accept(MinersBlock block) {
        return super.accept(block)
                && validTransactions((VCBlock) block);
    }

}

