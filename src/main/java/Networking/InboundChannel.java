package Networking;

import Networking.Runnables.Producable;
import Networking.Runnables.Producer;
import Messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.concurrent.TransferQueue;

public class InboundChannel implements Producable, Runnable {
    private static final Logger LOGGER = LogManager.getLogger();

    private TransferQueue<Message> queue;
    private Producer producer;

    private DatagramSocket socket;

    public InboundChannel(TransferQueue<Message> inputQueue) {
        queue = inputQueue;
        producer = new Producer("InboundChannel", queue, this);
        // Step 0: Create a socket to listen for incoming messages
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            LOGGER.error("Unable to open inbound UDP socket.");
            e.printStackTrace();
        }
    }

    public Message receive() throws IOException, ClassNotFoundException {
        // Step 1: Initialize the buffer
        byte[] buffer = new byte[5000];

        // Step 2: Generate the packet stub
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // Step 2: Receive message
        socket.receive(packet);

        // Step 3: De-serialize message
        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
        Message message = (Message) iStream.readObject();
        iStream.close();

        return message;
    }

    public InetSocketAddress getAddress() {
        try {
            InetAddress a = InetAddress.getLocalHost();
            int p = socket.getLocalPort();

            InetSocketAddress address = new InetSocketAddress(a, p);
            LOGGER.debug("Local address is: " + address);

            return address;
        } catch (UnknownHostException e) {
            LOGGER.error("Unable to get Local Host address");
            e.printStackTrace();
            return new InetSocketAddress(socket.getLocalPort());
        }
    }

    @Override
    public Message produce() {
        try {
            return receive();
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
