package Messages;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

public class JoinedMessage implements Message{
    private final UUID sender;
    private UUID recipient;
    private Map<UUID, InetSocketAddress> members;


    public JoinedMessage(UUID recipient, Map<UUID, InetSocketAddress> message, UUID sender) {
        this.recipient = recipient;
        this.sender = sender;
        this.members = message;
    }

    public Map<UUID, InetSocketAddress> getMembers() {
        return members;
    }

    @Override
    public void setRecipient(UUID recipient) {
        this.recipient = recipient;
    }

    @Override
    public UUID getSender() {
        return sender;
    }

    @Override
    public UUID getRecipient() {
        return recipient;
    }
}
