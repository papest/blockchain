package blockchain;

import java.util.concurrent.Callable;

public class Miner implements Callable<MinerBlockchain.MinersBlock> {
    static int numberOfMiners = 0;
    int number;
    MinerBlockchain minerBlockchain;

    public Miner(MinerBlockchain minerBlockchain) {
        number = numberOfMiners++;

        this.minerBlockchain = minerBlockchain;


    }

    @Override
    public MinerBlockchain.MinersBlock call() {
        MinerBlockchain.MinersBlock block;
        if (minerBlockchain.blockchain.isEmpty()) {
            block = minerBlockchain.new MinersBlock();
        } else {
            block = minerBlockchain
                    .new MinersBlock(minerBlockchain.blockchain.get(minerBlockchain.blockchain.size() - 1));
        }

        block.minerNumber = number;
        return block;
    }
}