package blockchain;

public class ChatMiner extends Miner{
    ChatBlockChain chatBlockChain;
    public ChatMiner(ChatBlockChain chatBlockchain) {
        super(chatBlockchain);
        this.chatBlockChain = chatBlockchain;

    }

    public ChatBlockChain.ChatBlock call(){
        ChatBlockChain.ChatBlock block;
        block = chatBlockChain.generateBlock(1);
        block.minerNumber = number;
        return block;
    }
}
