package Networking.Runnables;

import Messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

public class Producer implements Runnable {
    protected final Logger LOGGER = LogManager.getLogger();

    private String name;
    private volatile boolean active;
    private TransferQueue<Message> transferQueue;
    private Producable producer;

    public Producer(String name, TransferQueue<Message> transferQueue, Producable producer) {
        this.name = name;
        this.active = true;
        this.producer = producer;
        this.transferQueue = transferQueue;
    }

    @Override
    public void run() {
        while(active) {
            try {
                transferQueue.tryTransfer(producer.produce(), 1000, TimeUnit.MILLISECONDS);
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
