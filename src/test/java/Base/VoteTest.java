package Base;

import Networking.PeerNode;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class VoteTest {

    private Legislator l1;
    private Legislator l2;

    @BeforeEach
    void setUp() {
        Chamber chamber = new Chamber(new PeerNode());
        l1 = new Legislator(chamber);
        l2 = new Legislator(chamber);
    }

    @Test
    void nullVote() {
        Vote v1 = Vote.NullVote(l1);
        Assert.assertEquals((int)v1.getBallot().getKey(), -1);
        Assert.assertEquals(v1.getDecree(), BasicDecrees.BLANK_DECREE);
        //Assert.assertEquals(v1.getBallot(), BasicDecrees.BLANK_DECREE);
    }

    @Test
    void compareTo() {
        Vote nv = Vote.NullVote(l1);

        Ballot b1 = new Ballot(new BallotID(5, l1.getMemberID()),BasicDecrees.TRIVIAL_DECREE,new HashSet<>());
        Ballot b2 = new Ballot(new BallotID(10, l2.getMemberID()),BasicDecrees.TRIVIAL_DECREE,new HashSet<>());
        Vote v1 = new Vote(l1.getMemberID(), b1.getBallotID(), BasicDecrees.TRIVIAL_DECREE);
        Vote v2 = new Vote(l2.getMemberID(), b2.getBallotID(), BasicDecrees.TRIVIAL_DECREE);
        Assert.assertTrue(nv.compareTo(v1) < 0);
        Assert.assertTrue(v1.compareTo(v2) < 0);
        Assert.assertTrue(v2.compareTo(nv) > 0);
    }
}