package Base;

import Messages.*;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

import static org.junit.jupiter.api.Assertions.*;

class LegislatorTest {
    private Legislator l;
    int ballotID;

    @BeforeEach
    void setUp() {
        Chamber chamber = new Chamber(new LinkedTransferQueue<>());
        l = new Legislator(chamber);
        ballotID = 0;
    }

    /**
     * Test for Step 1 - Base case
     */
    @Test
    void nextBallot_0() {
        int b = l.nextBallot();
        assertEquals(ballotID, b);
    }

    /**
     * Test for Step 1 - Any other case
     */
    @Test
    void nextBallot_Any() {
        l.nextBallot();
        l.nextBallot();
        int b = l.nextBallot();
        assertEquals(b, l.getLedger().getLastTriedBallot());
    }

    /**
     * Test for Step 2
     */
    @Test
    void lastVote() {
        int bound = 1;
        Message m = new NextBallotMessage(l.getMemberID(),bound,l);
        try {
            LastVoteMessage lv = (LastVoteMessage) l.receive(m);
            assertEquals(bound, lv.getBound());
            assertEquals(lv.getVote().getDecree(), BasicDecrees.BLANK_DECREE);
            //TODO: test more complex cases
        } catch (UnknownMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for Step 3
     */
    @Test
    void processLastVote() {
        Vote v = Vote.NullVote(l);
        Pair<Integer, Vote> c = new Pair<>(ballotID, v);
        Message m = new LastVoteMessage(l.getMemberID(), c, l.getMemberID());
        l.nextBallot(); //Increment lastTriedBallot to simulate previous NextBallot message
        //TODO: test more complex cases
        try {
            BeginBallotMessage bb = (BeginBallotMessage) l.receive(m);
            assertEquals(ballotID, bb.getBallot().getBallotID());
            assertEquals(BasicDecrees.TRIVIAL_DECREE, bb.getBallot().getDecree());
        } catch (UnknownMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for Step 4
     */
    @Test
    void vote() {
        Decree d = BasicDecrees.TRIVIAL_DECREE;
        Set<UUID> q = new HashSet<>();
        q.add(l.getMemberID());
        Ballot b = new Ballot(1, d, q);
        Message m = new BeginBallotMessage(l.getMemberID(),b,l);
        l.getLedger().setNextBallot(b.getBallotID());
        try {
            VotedMessage v = (VotedMessage) l.receive(m);
            assertEquals(d, v.getVote().getDecree());
            //TODO: test more complex cases
        } catch (UnknownMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for Step 5
     */
    @Test
    void processVote() {
        //TODO: test the case it is not equal to quorum
        Decree d = BasicDecrees.TRIVIAL_DECREE;
        Set<UUID> q = new HashSet<>();
        q.add(l.getMemberID());
        processLastVote();
        Ballot b = new Ballot(ballotID, d, q);
        Vote v = new Vote(l, b, d);
        Message m = new VotedMessage(l.getMemberID(), v, l);
        try {
            SuccessMessage s = (SuccessMessage) l.receive(m);
            assertEquals(s.getBallot().getBallotID(), b.getBallotID());
        } catch (UnknownMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test for Step 6
     */
    @Test
    void updateLedger() {
        Decree d = BasicDecrees.TRIVIAL_DECREE;
        Set<UUID> q = new HashSet<>();
        Ballot b = new Ballot(ballotID, d, q);
        Message m = new SuccessMessage(b, l);
        try {
            SuccessMessage s = (SuccessMessage) l.receive(m);
            assertEquals(s.getBallot(), b);
        } catch (UnknownMessageException e) {
            e.printStackTrace();
        }
    }
}