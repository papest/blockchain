package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {


        ChatBlockChain chatBlockChain = new ChatBlockChain();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<ChatMiner> minersList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            minersList.add(new ChatMiner(chatBlockChain));
        }
        for (int i = 0; i < 5; i++) {
            try {
                while(!chatBlockChain.accept(executor.invokeAny(minersList))){}
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        System.out.println(chatBlockChain);

    }
}



