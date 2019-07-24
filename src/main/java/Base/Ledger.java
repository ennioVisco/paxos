package Base;

import Messages.LastVoteMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Ledger {
    private Legislator legislator;
    private Map<Integer,Vote> previousVotes;
    private List<LastVoteMessage> lastVoteMessages;

    private List<Ballot> approvedBallots;

    public Vote getLastVote(Integer max, Integer requester) {
        Vote bind = Vote.NullVote(legislator);
        for(Integer b: previousVotes.keySet()){
            if(b < max && bind.getBallot().getBallotID() < b) {
                bind = previousVotes.get(b);
            }
        }
        return bind;
    }

    public List<Ballot> getApprovedBallots() {
        return approvedBallots;
    }

    public void addApprovedBallot(Ballot ballot) {
        this.approvedBallots.add(ballot);
    }

    public void addVoteMessage(LastVoteMessage message) {
        lastVoteMessages.add(message);
    }

    public Integer getNewBallotId() {
        //TODO: must satisfy B1 rule
        return Collections.max(previousVotes.keySet()) + 1;
    }
}
