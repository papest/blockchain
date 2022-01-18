package blockchain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        VCBlockChain vcBlockChain = null ;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(

                new FileInputStream(System.getProperty("user.home")+ File.separator + "blockchain.txt"))) {
            vcBlockChain = (VCBlockChain) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

    }
        if (vcBlockChain == null) {
            vcBlockChain = new VCBlockChain();
        }
int minerCount = 4;
int blockCount = 1;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Miner<VCBlockChain.VCBlock>> minersList = new ArrayList<>();
        for (int i = 0; i < minerCount; i++) {
            minersList.add(new Miner<>(vcBlockChain));
        }
        for (int i = 0; i < blockCount; i++) {
           Miner<VCBlockChain.VCBlock> sender = minersList.get(ThreadLocalRandom.current().nextInt(minerCount));
                    sender.send(new Transaction(ThreadLocalRandom.current().nextInt(vcBlockChain.sumVC(sender)), sender,
                            minersList.get(ThreadLocalRandom.current().nextInt(minerCount))));
            try {
                while (!vcBlockChain.accept(executor.invokeAny(minersList))) {
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdownNow();
        System.out.println(vcBlockChain);

//        try(ObjectOutputStream oos = new ObjectOutputStream(
//                new FileOutputStream(System.getProperty("user.home")+ File.separator + "blockchain.txt"))){
//
//            oos.writeObject(vcBlockChain);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(vcBlockChain);
//
    }
}



