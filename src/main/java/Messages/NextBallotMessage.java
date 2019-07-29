package Messages;

import Base.BallotID;
import Base.Legislator;

import java.util.UUID;

public class NextBallotMessage implements Message {

    private final UUID sender;
    private UUID recipient;

    private final BallotID ballot;

    public NextBallotMessage(UUID recipient, BallotID message, UUID sender) {
        this.ballot = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public NextBallotMessage(UUID recipient, BallotID message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public BallotID getBallot() {
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
