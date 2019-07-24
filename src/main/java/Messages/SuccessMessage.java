package Messages;

import Base.Ballot;
import Base.Legislator;
import Base.Vote;

public class SuccessMessage implements Message {

    private final Integer sender;
    private Integer recipient;

    private final Ballot ballot;

    public SuccessMessage(Ballot message, Integer sender) {
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


    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }

    @Override
    public Integer getSender() {
        return sender;
    }

    @Override
    public Integer getRecipient() {
        return recipient;
    }
}
