package Messages;

import Base.Legislator;
import Base.Vote;
import javafx.util.Pair;

public class LastVoteMessage implements Message{
    private final Integer sender;
    private Integer recipient;

    private final Vote vote;
    private final Integer bound;

    public LastVoteMessage(Integer recipient, Pair<Integer,Vote> message, Integer sender) {
        this.recipient = recipient;
        this.bound = message.getKey();
        this.vote = message.getValue();
        this.sender = sender;
    }

    public LastVoteMessage(Integer recipient, Pair<Integer,Vote> message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Vote getVote() {
        return vote;
    }

    public Integer getBound() {
        return bound;
    }


    @Override
    public Integer getSender() {
        return sender;
    }

    @Override
    public Integer getRecipient() {
        return recipient;
    }

    @Override
    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }
}
