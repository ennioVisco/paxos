package Base;

import Networking.PeerNode;
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
        Chamber chamber = new Chamber(new PeerNode());
        Legislator l = new Legislator(chamber);
        ledger = new Ledger(l);
        Set<UUID> ls = new HashSet<>();
        ls.add(l.getMemberID());
        BallotID bid = new BallotID(10,l.getMemberID());
        Ballot b = new Ballot(bid, BasicDecrees.TRIVIAL_DECREE, ls);
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
        BallotID max = ledger.getNewBallotId();
        BallotID prev = ledger.getPreviousVote().getBallot().getBallotID();
        assertTrue(max.compareTo(prev) > 0);
    }
}