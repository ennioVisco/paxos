package Networking;

import Messages.Message;

import java.net.InetSocketAddress;

/**
 * This is the message packet used to communicate with the network layer:
 * it's purpose is to successfully identify the recipient
 */
public class UDPMessage {

    private final Message message;
    private final InetSocketAddress recipient;

    public UDPMessage(Message message, InetSocketAddress recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    public InetSocketAddress getRecipient() {
        return recipient;
    }

    public Message getMessage() {
        return message;
    }
}
