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

import static org.junit.jupiter.api.Assertions.*;

class BallotTest {

    private Legislator l1;
    private Legislator l2;
    private Legislator l3;
    private HashSet<UUID> ls;
    private Ballot b;

    @BeforeEach
    void setUp() {
        l1 = new Legislator();
        l2 = new Legislator();
        l3 = new Legislator();
        ls = new HashSet<>();
        ls.add(l1.getMemberID());
        ls.add(l2.getMemberID());
        b = new Ballot(1, BasicDecrees.TRIVIAL_DECREE, ls);
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
        Ballot b2 = new Ballot(5,BasicDecrees.TRIVIAL_DECREE, ls);
        Assert.assertTrue(b.compareTo(b2) < 0);
    }

}