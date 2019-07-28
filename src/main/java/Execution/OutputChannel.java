package Execution;

import Execution.Runnables.Consumable;
import Execution.Runnables.Consumer;
import Messages.Message;
import Networking.Peer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TransferQueue;

public class OutputChannel implements Consumable, Runnable {
    private final Logger LOGGER = LogManager.getLogger();

    private TransferQueue<Message> queue;
    private Consumer consumer;
    private Map<UUID, InetAddress> peers;

    public OutputChannel(TransferQueue<Message> outputQueue, Map<UUID, InetAddress> addressees) {
        queue = outputQueue;
        consumer = new Consumer("OutputChannel", queue, this);
        peers = addressees;
    }

    @Override
    public void consume(Message message) {
        LOGGER.debug("Sending message " + message);
        try {
            Peer.send(message, peers.get(message.getRecipient()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        consumer.run();
    }
}
