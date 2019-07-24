package Base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * The getBallot class is a data class defined like of 4 components:
 * 1) B_dec := The decree being voted
 * 2) B_qrm := A nonempty set of legislators that forms the quorum
 * 3) B_vot := A subset of B_qrm that casted a vote
 * 4) B_bal := A ballot number (id)
 */
public class Ballot implements Comparable<Ballot> {
    //TODO: Probably 'Character' (i.e. positive integers) is better than Integer to exploit all the numbers,
    // after all a bijection can easily be defined between Integers and positive numbers.
    private final Integer ballotID;
    private final Decree decree;
    private final Set<Integer> quorum;
    private Set<Vote> votes;

    @Contract(pure = true)
    public Ballot(int ballotID, Decree decree, Set<Integer> quorum) {
        this.ballotID = ballotID;
        this.decree = decree;
        this.quorum = quorum;
        this.votes = new HashSet<>();
    }

    /**
     * This method is the only modifier for the only mutable parameter of a ballot
     * i.e. the number of people who voted.
     * @param vote legislator who wants to pass the decree of the current ballot.
     */
    public void addVote(Vote vote) {
        if(quorum.contains(vote.getLegislator()))
            votes.add(vote);
    }

    public boolean checkStatus() {
        return quorum.equals(votes);
    }

    @Override
    public int compareTo(@NotNull Ballot b) {
        return (ballotID.compareTo(b.getBallotID()));
    }

    public int getBallotID() {
        return ballotID;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Set<Integer> getQuorum() {
        return quorum;
    }

    public Decree getDecree() {
        return decree;
    }
}
