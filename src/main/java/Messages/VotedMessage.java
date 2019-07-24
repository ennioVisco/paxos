package Messages;

import Base.Legislator;
import Base.Vote;

public class VotedMessage implements Message {

    private final Integer sender;
    private Integer recipient;

    private final Vote vote;

    public VotedMessage(Integer recipient, Vote message, Integer sender) {
        this.vote = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public VotedMessage(Integer recipient, Vote message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Vote getVote() {
        return vote;
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