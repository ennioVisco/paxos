package Networking;

import Testing.Generics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class TrackerNodeTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    void run() {
        TrackerNode tracker = new TrackerNode();
        Runnable runnable = tracker::run;
        Thread t = new Thread(runnable);
        t.start();
        Generics.trySleep(500);

        PeerNode p1 = new PeerNode();
        PeerNode p2 = new PeerNode();

        new Thread(p1).start();
        new Thread(p2).start();
        Generics.trySleep(11000);

        LOGGER.debug("First result:");
        Assert.assertEquals(2, p1.getChamber().getMembers().size());
        LOGGER.debug("Second result:");
        Assert.assertEquals(2, p2.getChamber().getMembers().size());
    }
}