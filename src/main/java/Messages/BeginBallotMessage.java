package Messages;

import Base.Ballot;
import Base.Legislator;

import java.util.UUID;

public class BeginBallotMessage implements Message {

    private final UUID sender;
    private UUID recipient;

    private final Ballot ballot;

    public BeginBallotMessage(UUID recipient, Ballot message, UUID sender) {
        this.ballot = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public BeginBallotMessage(UUID recipient, Ballot message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Ballot getBallot() {
        return ballot;
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
