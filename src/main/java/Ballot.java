import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

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
    private final HashSet<Legislator> quorum;
    private HashSet<Legislator> voters;

    @Contract(pure = true)
    public Ballot(int ballotID, Decree decree, HashSet<Legislator> quorum) {
        this.ballotID = ballotID;
        this.decree = decree;
        this.quorum = quorum;
        this.voters = new HashSet<>();
    }

    /**
     * This method is the only modifier for the only mutable parameter of a ballot
     * i.e. the number of people who voted.
     * @param voter legislator who wants to pass the decree of the current ballot.
     */
    public void addVote(Legislator voter) {
        if(quorum.contains(voter))
            voters.add(voter);
    }

    public boolean checkStatus() {
        return quorum.equals(voters);
    }

    @Override
    public int compareTo(@NotNull Ballot b) {
        return (ballotID.compareTo(b.getBallotID()));
    }

    public int getBallotID() {
        return ballotID;
    }

    public HashSet<Legislator> getVoters() {
        return voters;
    }

    public HashSet<Legislator> getQuorum() {
        return quorum;
    }

    public Decree getDecree() {
        return decree;
    }
}
