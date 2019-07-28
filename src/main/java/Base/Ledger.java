package Base;

import Messages.LastVoteMessage;
import java.util.*;

/**
 * The ledgers serve as the private persistent memory of legislators.
 * They collect some data (it varies depending on the version of the protocol)
 * Related to previous approved decrees, to previous votes and previous messages sent.
 */
public class Ledger {
    private List<Ballot> approvedBallots;
    private Set<Vote> lastVotesReceived;

    private Integer lastTriedBallot;
    private Vote previousVote;
    private Integer nextBallotID;

    Ledger(Legislator legislator) {
        approvedBallots = new ArrayList<>();
        lastVotesReceived = new HashSet<>();

        previousVote = Vote.NullVote(legislator);
        lastTriedBallot = -1;
        nextBallotID = -1;
    }

    /**
     * LastVotes are messages containing the last vote a Legislator in the quorum had previously expressed.
     * LastVotes are used by Legislators to decide whether to start the Ballot or not.
     */
    Set<Vote> getLastVotesReceived() {
        return lastVotesReceived;
    }

    /**
     * LastVotes are messages containing the last vote a Legislator in the quorum had previously expressed.
     * When a new LastVote is received, its content is noted on the back of the ledger.
     * @param vote the lastVote received corresponding to the requested bound
     */
    void addLastVoteReceived(Vote vote) {
        lastVotesReceived.add(vote);
    }

    /**
     * LastVotes are messages containing the last vote a Legislator in the quorum had previously expressed.
     * When a ballot starts, the starting legislator clears the back of his ledger to prepare for incoming
     * messages.
     */
    void emptyLastVotesReceived() {
        lastVotesReceived.clear();
    }

    /**
     * Method for updating the last vote expressed.
     * @param vote the new vote to be added, if newer
     */
    void addPreviousVote(Vote vote) {
        //TODO: verify this is right (step 4)
        Ballot previous = previousVote.getBallot();
        Ballot current = vote.getBallot();
        if(current.getBallotID() < previous.getBallotID())
            previousVote = vote;
    }

    Vote getPreviousVote() {
        return previousVote;
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

    /**
     * Used to record the ballot corresponding to the LastVote expressed.
     * @param bid Bound for the Ballot ID.
     */
    void setNextBallot(Integer bid) {
        nextBallotID = bid;
    }

    public Integer getLastTriedBallot() {
        return lastTriedBallot;
    }

    public Integer getNextBallotID() {
        return nextBallotID;
    }

    Integer getNewBallotId() {
        //TODO: must satisfy B1 rule
        lastTriedBallot++;
        return lastTriedBallot;
    }
}
