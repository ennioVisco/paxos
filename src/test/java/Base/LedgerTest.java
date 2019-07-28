package Base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

import static org.junit.jupiter.api.Assertions.*;

class LedgerTest {

    Ledger ledger;

    @BeforeEach
    void setUp() {
        Chamber chamber = new Chamber(new LinkedTransferQueue<>());
        Legislator l = new Legislator(chamber);
        ledger = new Ledger(l);
        Set<UUID> ls = new HashSet<>();
        ls.add(l.getMemberID());
        Ballot b = new Ballot(10, BasicDecrees.TRIVIAL_DECREE, ls);
        Vote v = new Vote (l, b, BasicDecrees.TRIVIAL_DECREE);
        ledger.addPreviousVote(v);
    }

    /*@Test
    void gettLasVote() {
        Vote lv = ledger.getPreviousVote(20);
        assertEquals(10, lv.getBallot().getBallotID());
        lv = ledger.getPreviousVote(5);
        assertEquals(-1, lv.getBallot().getBallotID());
    }*/

    @Test
    void getNewBallotId() {
        //TODO: verify this is right
        Integer max = ledger.getNewBallotId();
        Integer prev = ledger.getPreviousVote().getBallot().getBallotID();
        assertTrue(max > prev);
    }
}