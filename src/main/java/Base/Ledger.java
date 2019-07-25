package Base;

import Messages.LastVoteMessage;
import java.util.*;

/**
 * The ledgers serve as the private persistent memory of legislators.
 * They collect some data (it varies depending on the version of the protocol)
 * Related to previous approved decrees, to previous votes and previous messages sent.
 */
class Ledger {
    private Legislator legislator;
    private Map<Integer,Vote> previousVotes;
    private Set<LastVoteMessage> lastVotesSent;
    private Set<Vote> lastVotesReceived;

    private List<Ballot> approvedBallots;

    Ledger(Legislator legislator) {
        approvedBallots = new ArrayList<>();
        lastVotesReceived = new HashSet<>();
        previousVotes = new HashMap<>();
        lastVotesSent = new HashSet<>();
        this.legislator = legislator;
    }

    Vote getLastVote(Integer max) {
        Vote bind = Vote.NullVote(legislator);
        for(Integer b: previousVotes.keySet()){
            if(b < max && bind.getBallot().getBallotID() < b) {
                bind = previousVotes.get(b);
            }
        }
        return bind;
    }

    public Set<Vote> getLastVotesReceived() {
        return lastVotesReceived;
    }

    public void addLastVoteReceived(Vote vote) {
        lastVotesReceived.add(vote);
    }

    public void emptyLastVotesReceived() {
        lastVotesReceived.clear();
    }

    public Map<Integer, Vote> getPreviousVotes() {
        return previousVotes;
    }

    void addVote(Vote vote) {
        previousVotes.put(vote.getBallot().getBallotID(),vote);
    }

    /**
     * Approved ballots are ballots that must be acquired by all members of the Chamber as their
     * decree has now become law.
     */
    List<Ballot> getApprovedBallots() {
        return approvedBallots;
    }

    /**
     * Approved ballots are ballots that must be acquired by all members of the Chamber as their
     * decree has now become law.
     * @param ballot the newly approved ballot.
     */
    void addApprovedBallot(Ballot ballot) {
        approvedBallots.add(ballot);
    }

    void addVoteMessage(LastVoteMessage message) {
        lastVotesSent.add(message);
    }

    Integer getNewBallotId() {
        //TODO: must satisfy B1 rule
        if(!previousVotes.keySet().isEmpty())
            return Collections.max(previousVotes.keySet())  + 1;
        return 1;
    }
}
