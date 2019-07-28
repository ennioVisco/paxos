package Execution.Runnables;

import Messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TransferQueue;

public class Consumer implements Runnable {
    protected final Logger LOGGER = LogManager.getLogger();

    private final String name;
    private volatile boolean active;
    private final Consumable consumer;
    private TransferQueue<Message> transferQueue;

    public Consumer(String name, TransferQueue<Message> transferQueue, Consumable consumer) {
        this.name = name;
        this.active = true;
        this.consumer = consumer;
        this.transferQueue = transferQueue;
    }

    @Override
    public void run() {
        while(active) {
            Message m;
            try {
                m = transferQueue.take();
                consumer.consume(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
