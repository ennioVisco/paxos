import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BallotTest {

    private Legislator l1;
    private Legislator l2;
    private Legislator l3;
    private HashSet<Legislator> ls;
    private Ballot b;

    @BeforeEach
    void setUp() {
        l1 = new Legislator();
        l2 = new Legislator();
        l3 = new Legislator();
        ls = new HashSet<>();
        ls.add(l1);
        ls.add(l2);
        b = new Ballot(1, BasicDecrees.TRIVIAL_DECREE, ls);
    }

    @Test
    void addVote() {
        assertFalse(b.getVoters().equals(b.getQuorum()));
        b.addVote(l1);
        b.addVote(l2);
        assertTrue(b.getVoters().equals(b.getQuorum()));
        b.addVote(l3);
        assertTrue(b.getVoters().equals(b.getQuorum()));
    }

    @Test
    void checkStatus() {
        assertFalse(b.checkStatus());
        b.addVote(l1);
        b.addVote(l2);
        assertTrue(b.checkStatus());
    }

    @Test
    void compareTo() {
        Ballot b2 = new Ballot(5,BasicDecrees.TRIVIAL_DECREE, ls);
        Assert.assertTrue(b.compareTo(b2) < 0);
    }

}