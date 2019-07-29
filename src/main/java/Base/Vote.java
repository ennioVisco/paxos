package Base;

import javafx.util.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * The Base.Vote class is an immutable data class defined like this v:= <l,b,d>
 * 1) l is the legislator voting
 * 2) b is the current ballot
 * 3) d is the decree being voted
 */
public class Vote implements Comparable<Vote> {

    private Legislator legislator;
    private Ballot ballot;
    private Decree decree;

    @Contract(pure = true)
    public Vote(Legislator legislator, Ballot ballot, Decree decree) {
        this.legislator = legislator;
        this.ballot = ballot;
        this.decree = decree;
    }

    @NotNull
    @Contract(pure = true)
    public static Vote NullVote(Legislator l) {
        BallotID bid = new BallotID(-1, l.getMemberID());
        Ballot ballot = new Ballot(bid, BasicDecrees.BLANK_DECREE, new HashSet<>());
        return new Vote (l, ballot, BasicDecrees.BLANK_DECREE);
    }

    public Legislator getLegislator() {
        return legislator;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public Decree getDecree() {
        return decree;
    }

    @Override
    public int compareTo(@NotNull Vote v) {
        return (ballot.compareTo(v.getBallot()));
    }
}
