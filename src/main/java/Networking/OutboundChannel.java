package Networking;

import Networking.Runnables.Consumable;
import Networking.Runnables.Consumer;
import Messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.TransferQueue;

public class OutboundChannel implements Consumable<UDPMessage>, Runnable {
    private static final Logger LOGGER = LogManager.getLogger();

    private TransferQueue<UDPMessage> queue;
    private Consumer consumer;

    public OutboundChannel(TransferQueue<UDPMessage> outputQueue) {
        queue = outputQueue;
        consumer = new Consumer<>("OutboundChannel", queue, this);
    }

    public static void send(Message message, InetSocketAddress receiver) throws IOException {
        byte[] buffer;
        // Step 1: Create the socket object for carrying the data.
        DatagramSocket sendSocket = new DatagramSocket();

        // Step 2: Serialize the object to a byte array.
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(message);
        oo.close();
        buffer = bStream.toByteArray();

        // Step 3 : Create the datagramPacket for sending the data.
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiver);

        // Step 4 : Invoke the send call to actually send the data.
        LOGGER.info("Sending | " + message + " | to > " + receiver);
        sendSocket.send(packet);
    }

    @Override
    public void consume(UDPMessage message) {
        LOGGER.debug("Sending message " + message);
        try {
            send(message.getMessage(), message.getRecipient());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        consumer.run();
    }
}
