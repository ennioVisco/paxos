package Messages;

import Base.Legislator;

public class NextBallotMessage implements Message {

    private final Integer sender;
    private Integer recipient;

    private final Integer ballot;

    public NextBallotMessage(Integer recipient, Integer message, Integer sender) {
        this.ballot = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public NextBallotMessage(Integer recipient, Integer message, Legislator sender) {
        this(recipient, message, sender.getMemberID());
    }

    public Integer getBallot() {
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
