package Networking;

import Base.BallotID;
import Base.Legislator;
import Messages.Message;
import Messages.NextBallotMessage;
import Testing.Generics;
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
        PeerNode peer = new PeerNode(true);
        new Thread(peer).start();
        Generics.trySleep(100);

        Legislator l = peer.getChamber().getLocalMembers().iterator().next();
        UUID currID = l.getMemberID();
        Message m = new NextBallotMessage(currID, nextID, UUID.randomUUID());
        LOGGER.info("Simulating message enqueue " + m);
        peer.handleMessage(m);

        Assert.assertEquals(10, (long) l.getLedger().getNextBallotID().getKey());
        Assert.assertEquals(0, (long) l.nextBallot().getKey());
    }
}