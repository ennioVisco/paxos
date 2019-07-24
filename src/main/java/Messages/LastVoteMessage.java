package Messages;

import Base.Legislator;
import Base.Vote;

public class LastVoteMessage implements Message{
    private Legislator recipient;
    private Vote message;

    public LastVoteMessage(Legislator r, Vote v) {
        recipient = r;
        message = v;
    }

    public Legislator getRecipient() {
        return recipient;
    }

    public Vote getMessage() {
        return message;
    }
}
