package Messages;

import Base.Legislator;

import java.util.UUID;

public class AliveMessage implements Message {

    private final UUID sender;
    private UUID recipient;

    public AliveMessage(UUID recipient, UUID sender) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public AliveMessage(UUID recipient, Legislator sender) {
        this(recipient, sender.getMemberID());
    }

    @Override
    public UUID getSender() {
        return sender;
    }

    @Override
    public UUID getRecipient() {
        return recipient;
    }

    @Override
    public void setRecipient(UUID recipient) {
        this.recipient = recipient;
    }
}
