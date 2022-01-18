package blockchain;

import java.io.Serializable;

public class Transaction extends Message implements Serializable {
    int sum;
    Miner<VCBlockChain.VCBlock> sender;
    Miner<VCBlockChain.VCBlock> recipient;



    @Override
    public String toString() {
        return String.format("Miner%s sent to miner%s %sVC\n", sender.number,
                recipient.number, sum);

    }

    public Transaction(int sum, Miner<VCBlockChain.VCBlock> sender, Miner<VCBlockChain.VCBlock> recipient) {
        this("", String.format("Miner%s sent to miner%s %sVC\n", sender.number,
                recipient.number, sum), sender.sign(String.format("Miner%s sent to miner%s %sVC\n", sender.number,
                recipient.number, sum)));

        this.signature = sender.sign(String.format("Miner%s sent to miner%s %sVC\n", sender.number,
                recipient.number, sum));
        this.sum = sum;
        this.sender = sender;
        this.recipient = recipient;
    }

    private Transaction(String username, String message, byte[] signature) {
        super(username, message, signature);
    }

    public int getSum() {
        return sum;
    }
}
