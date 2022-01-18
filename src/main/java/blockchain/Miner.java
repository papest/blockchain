package blockchain;

import blockchain.MinerBlockchain.InterfaceBlock;
import blockchain.MinerBlockchain.MinersBlock;


import java.io.Serializable;
import java.security.*;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Miner<T extends MinersBlock> implements Callable<T>, Serializable {

    final int INITIAL_SUM = 100;
    static int numberOfMiners = 0;
    int number;
    int wallet = INITIAL_SUM;
    MinerBlockchain minerBlockchain;
    GenerateKeys gk;

    {
        try {
            gk = new GenerateKeys(1024);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }


    transient Map<Class<? extends MinerBlockchain>, Supplier<InterfaceBlock>> firstBlockSupplierMap = Map.of(MinerBlockchain.class,
            () -> minerBlockchain.new MinersBlock(),
            ChatBlockChain.class, () -> ((ChatBlockChain) minerBlockchain).new ChatBlock(),
            VCBlockChain.class, () -> {
                VCBlockChain.VCBlock vcBlock = ((VCBlockChain) minerBlockchain).new VCBlock();
                vcBlock.miner = (Miner<VCBlockChain.VCBlock>) this;
                return vcBlock;
    });
    transient Map<Class<? extends MinerBlockchain>, java.util.function.UnaryOperator<InterfaceBlock>> secondBlockSupplierMap = Map.of(MinerBlockchain.class,
            a -> minerBlockchain.new MinersBlock((Block) a),
            ChatBlockChain.class, a -> ((ChatBlockChain) minerBlockchain).new ChatBlock((Block) a),
            VCBlockChain.class, a -> {
                VCBlockChain.VCBlock vcBlock = ((VCBlockChain) minerBlockchain).new VCBlock((Block) a);
                vcBlock.miner = (Miner<VCBlockChain.VCBlock>) this;
                return vcBlock;
    });


    public Miner(MinerBlockchain mBlockchain) {
        number = numberOfMiners++;
        this.minerBlockchain = mBlockchain;

    }
    public void send (Transaction transaction) {
        ((VCBlockChain)   minerBlockchain).receive(transaction, this);

    }

    public void send(String messageMessage) {
        ((ChatBlockChain) minerBlockchain).receive(new Message("miner # " + this.number, messageMessage
                        , sign(messageMessage)),
                this);

    }

    @Override
    public T call() {
        T block;
        if (minerBlockchain.blockchain.isEmpty()) {
            block = (T) firstBlockSupplierMap.get(minerBlockchain.getClass()).get();
        } else {
            block = (T) secondBlockSupplierMap.get(minerBlockchain.getClass()).apply(minerBlockchain.blockchain
                    .get(minerBlockchain.blockchain.size() - 1));
        }

        block.minerNumber = number;
        return block;
    }

    public PublicKey getPublicKey() {
        return gk.getPublicKey();
    }

    public byte[] sign(String data) {
        Signature rsa = null;
        try {
            rsa = Signature.getInstance("SHA1withRSA");

            rsa.initSign(gk.getPrivateKey());
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }
}
