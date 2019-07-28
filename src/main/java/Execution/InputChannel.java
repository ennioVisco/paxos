package Execution;

import Execution.Runnables.Producable;
import Execution.Runnables.Producer;
import Messages.Message;
import Networking.Peer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TransferQueue;

public class InputChannel implements Producable, Runnable {

    private TransferQueue<Message> queue;
    private Producer producer;

    public InputChannel(TransferQueue<Message> inputQueue) {
        queue = inputQueue;
        producer = new Producer("InputChannel", queue, this);
    }

    @Override
    public Message produce() {
        try {
            return Peer.receive();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        producer.run();
    }
}
