package Execution;

import Base.BallotID;
import Base.Legislator;
import Messages.Message;
import Messages.NextBallotMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class PeerNodeTest {
    private static final Logger LOGGER = LogManager.getLogger();
    private BallotID nextID = new BallotID(10, UUID.randomUUID());

    @Test
    void run() {
        PeerNode peer = new PeerNode();
        Runnable runnable = peer::run;
        Thread t = new Thread(runnable);
        t.start();

        try {
            Thread.sleep(4000);
            Message m = new NextBallotMessage(peer.getLegislator().getMemberID(), nextID, UUID.randomUUID());
            LOGGER.info("Simulating message enqueue " + m);
            peer.enqueueInput(m);
            Thread.sleep(6000);
            Legislator l = peer.getLegislator();
            Assert.assertEquals(10, (long) l.getLedger().getNextBallotID().getKey());
            Assert.assertEquals(0, (long) l.nextBallot().getKey());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}