package Messages;

import Base.Legislator;
import Base.Vote;

import java.util.UUID;

public class VotedMessage implements Message {

    private final UUID sender;
    private UUID recipient;

    private final Vote vote;

    public VotedMessage(UUID recipient, Vote message, UUID sender) {
        this.vote = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public VotedMessage(UUID recipient, Vote message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Vote getVote() {
        return vote;
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