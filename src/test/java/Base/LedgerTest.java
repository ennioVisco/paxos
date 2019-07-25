package Base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LedgerTest {

    Ledger ledger;

    @BeforeEach
    void setUp() {
        Legislator l = new Legislator();
        ledger = new Ledger(l);
        Set<UUID> ls = new HashSet<>();
        ls.add(l.getMemberID());
        Ballot b = new Ballot(10, BasicDecrees.TRIVIAL_DECREE, ls);
        Vote v = new Vote (l, b, BasicDecrees.TRIVIAL_DECREE);
        ledger.addVote(v);
    }

    @Test
    void getLastVote() {
        Vote lv = ledger.getLastVote(20);
        assertEquals(10, lv.getBallot().getBallotID());
        lv = ledger.getLastVote(5);
        assertEquals(-1, lv.getBallot().getBallotID());
    }

    @Test
    void getNewBallotId() {
        Integer max = ledger.getNewBallotId();
        for(Integer bid: ledger.getPreviousVotes().keySet())
            assertTrue(max > bid);
    }
}