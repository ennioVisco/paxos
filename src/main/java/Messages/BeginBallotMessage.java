package Messages;

import Base.Ballot;
import Base.Legislator;

public class BeginBallotMessage implements Message {

    private final Integer sender;
    private Integer recipient;

    private final Ballot ballot;

    public BeginBallotMessage(Integer recipient, Ballot message, Integer sender) {
        this.ballot = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public BeginBallotMessage(Integer recipient, Ballot message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Ballot getBallot() {
        return ballot;
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
