package Messages;

import Base.Legislator;
import Base.Vote;
import javafx.util.Pair;

import java.util.UUID;

public class LastVoteMessage implements Message{
    private final UUID sender;
    private UUID recipient;

    private final Vote vote;
    private final Integer bound;

    public LastVoteMessage(UUID recipient, Pair<Integer,Vote> message, UUID sender) {
        this.recipient = recipient;
        this.bound = message.getKey();
        this.vote = message.getValue();
        this.sender = sender;
    }

    public LastVoteMessage(UUID recipient, Pair<Integer,Vote> message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Vote getVote() {
        return vote;
    }

    public Integer getBound() {
        return bound;
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
