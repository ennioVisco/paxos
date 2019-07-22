import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class VoteTest {

    private Legislator l1;
    private Legislator l2;

    @BeforeEach
    void setUp() {
        l1 = new Legislator();
        l2 = new Legislator();
    }

    @Test
    void nullVote() {
        Vote v1 = Vote.NullVote(l1);
        Assert.assertEquals(v1.getBallot().getBallotID(), -1);
        Assert.assertEquals(v1.getDecree(), BasicDecrees.NULL_DECREE);
        Assert.assertEquals(v1.getBallot().getDecree(), BasicDecrees.NULL_DECREE);
    }

    @Test
    void compareTo() {
        Vote nv = Vote.NullVote(l1);
        Ballot b1 = new Ballot(1,BasicDecrees.TRIVIAL_DECREE,new HashSet<>());
        Ballot b2 = new Ballot(5,BasicDecrees.TRIVIAL_DECREE,new HashSet<>());
        Vote v1 = new Vote(l1, b1, BasicDecrees.TRIVIAL_DECREE);
        Vote v2 = new Vote(l2, b2, BasicDecrees.TRIVIAL_DECREE);
        Assert.assertTrue(nv.compareTo(v1) < 0);
        Assert.assertTrue(v1.compareTo(v2) < 0);
        Assert.assertTrue(v2.compareTo(nv) > 0);
    }
}