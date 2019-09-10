package Execution;

import Base.Chamber;
import Base.Decree;
import Base.Legislator;
import Messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class PeerNode {
    private static final Logger LOGGER = LogManager.getLogger();

    private TransferQueue<Message> oQueue;
    private TransferQueue<Message> iQueue;

    public Legislator legislator;

    private volatile boolean active;

    public static void main(String[] args) {
        PeerNode node = new PeerNode();
        node.run();
    }

    public PeerNode() {
        // Initialize queues
        oQueue = new LinkedTransferQueue<>();
        iQueue = new LinkedTransferQueue<>();
        active = true;
    }

    public void run() {
        Chamber chamber = new Chamber(iQueue);
        legislator = new Legislator(chamber);

        //Initialize threads
        Runnable inputChannel = new InputChannel(iQueue);
        Runnable outputChannel = new OutputChannel(oQueue, chamber.getMembers());
        Thread inputManager = new Thread(inputChannel);
        Thread outputManager = new Thread(outputChannel);

        //Run threads
        inputManager.start();
        outputManager.start();

        while(active) {
            try {
                Message m = iQueue.take();
                LOGGER.info("Received a new message");
                chamber.deliver(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!active)
                break;
        }
    }

    public void requestDecree(Decree decree) {
        LOGGER.info("Requesting decree:" + decree);
        legislator.requestDecree(decree);
    }

    public void shutdown() {
        LOGGER.info("Shutting down the peer.");
        active = false;
    }

    public Legislator getLegislator() {
        return legislator;
    }

    public void enqueueInput(Message m) {
        iQueue.add(m);
    }

    public void enqueueOutput(Message m) {
        iQueue.add(m);
    }
}
