package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {


        MinerBlockchain minerBlockchain = new MinerBlockchain();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Miner> minersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            minersList.add(new Miner(minerBlockchain));
        }
        for (int i = 0; i < 5; i++) {
            try {
                while(!minerBlockchain.accept(executor.invokeAny(minersList))){}
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        System.out.println(minerBlockchain);
    }
}




