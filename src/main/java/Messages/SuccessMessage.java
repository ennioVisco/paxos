package Messages;

import Base.Ballot;
import Base.Legislator;
import Base.Vote;

import java.util.UUID;

public class SuccessMessage implements Message {

    private final UUID sender;
    private UUID recipient;

    private final Ballot ballot;

    public SuccessMessage(Ballot message, UUID sender) {
        this.ballot = message;
        this.sender = sender;
        this.recipient = null;
    }

    public SuccessMessage(Ballot message, Legislator sender) {
        this(message, sender.getMemberID());
    }

    public Ballot getBallot() {
        return ballot;
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
