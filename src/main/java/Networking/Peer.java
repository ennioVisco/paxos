package Networking;

import Base.Settings;
import Messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Peer {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void send(Message message, InetAddress receiver) throws IOException {
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
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiver, Settings.PORT);

        // Step 4 : Invoke the send call to actually send the data.
        LOGGER.info("Sending " + message);
        sendSocket.send(packet);
    }

    public static Message receive() throws IOException, ClassNotFoundException {
        // Step 1: Initialize the buffer
        byte[] buffer = new byte[5000];

        // Step 2: Create a socket to listen for incoming messages
        DatagramSocket socket = new DatagramSocket(Settings.PORT);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // Step 3: Receive message
        socket.receive(packet);

        // Step 4: De-serialize message
        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
        Message message = (Message) iStream.readObject();
        iStream.close();

        return message;
    }
}
