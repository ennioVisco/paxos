package Base;

import Base.Ballot;
import Base.BasicDecrees;
import Base.Legislator;
import Base.Vote;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

import static org.junit.jupiter.api.Assertions.*;

class BallotTest {

    private Legislator l1;
    private Legislator l2;
    private Legislator l3;
    private HashSet<UUID> ls;
    private Ballot b;

    @BeforeEach
    void setUp() {
        Chamber chamber = new Chamber(new LinkedTransferQueue<>());
        l1 = new Legislator(chamber);
        l2 = new Legislator(chamber);
        l3 = new Legislator(chamber);
        ls = new HashSet<>();
        ls.add(l1.getMemberID());
        ls.add(l2.getMemberID());
        BallotID bid = new BallotID(1,l1.getMemberID());
        b = new Ballot(bid, BasicDecrees.TRIVIAL_DECREE, ls);
    }

    @Test
    void addVote() {
        assertNotEquals(b.getVotes().size(), b.getQuorum().size());
        b.addVote(new Vote(l1, b, BasicDecrees.TRIVIAL_DECREE));
        b.addVote(new Vote(l2, b, BasicDecrees.TRIVIAL_DECREE));
        assertEquals(b.getVotes().size(), b.getQuorum().size());
        b.addVote(new Vote(l3, b, BasicDecrees.TRIVIAL_DECREE));
        assertEquals(b.getVotes().size(), b.getQuorum().size());
    }

    @Test
    void checkStatus() {
        assertFalse(b.checkStatus());
        b.addVote(new Vote(l1, b, BasicDecrees.TRIVIAL_DECREE));
        b.addVote(new Vote(l2, b, BasicDecrees.TRIVIAL_DECREE));
        assertTrue(b.checkStatus());
    }

    @Test
    void compareTo() {
        BallotID bid = new BallotID(5,l1.getMemberID());
        Ballot b2 = new Ballot(bid,BasicDecrees.TRIVIAL_DECREE, ls);
        Assert.assertTrue(b.compareTo(b2) < 0);
    }

}