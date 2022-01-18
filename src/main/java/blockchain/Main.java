package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        String[] messages = {
                "Ау!",
                "Есть кто?",
                "Пришлите немного денег!",
                "Скоро Новый Год!",
                "Да, да..",
                "Какой-то сумасшедший дом!"
        };


        ChatBlockChain chatBlockChain = new ChatBlockChain();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Miner<ChatBlockChain.ChatBlock>> minersList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            minersList.add(new Miner<>(chatBlockChain));
        }
        for (int i = 0; i < 5; i++) {
            minersList.get(ThreadLocalRandom.current().nextInt(4))
                    .send(messages[ThreadLocalRandom.current().nextInt(messages.length)]);
            try {
                while (!chatBlockChain.accept(executor.invokeAny(minersList))) {
                }
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



