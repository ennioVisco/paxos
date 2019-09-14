package Networking.Runnables;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TransferQueue;

public class Consumer<T> implements Runnable {
    protected final Logger LOGGER = LogManager.getLogger();

    private final String name;
    private volatile boolean active;
    private final Consumable<T> consumer;
    private TransferQueue<T> transferQueue;

    public Consumer(String name, TransferQueue<T> transferQueue, Consumable<T> consumer) {
        this.name = name;
        this.active = true;
        this.consumer = consumer;
        this.transferQueue = transferQueue;
    }

    @Override
    public void run() {
        while(active) {
            T message;
            try {
                message = transferQueue.take();
                consumer.consume(message);
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
