package Execution;

import Base.Chamber;
import Base.Legislator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.util.concurrent.LinkedTransferQueue;

class TrackerNodeTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    void run() {
        try {
            TrackerNode tracker = new TrackerNode();
            Runnable runnable = tracker::run;
            Thread t = new Thread(runnable);
            t.start();

            Thread.sleep(4000);
            Chamber chamber = new Chamber(new LinkedTransferQueue<>());
            new Legislator(chamber);
            new Legislator(chamber);
            Assert.assertEquals(2, chamber.getMembers().size());
        } catch (InterruptedException e) {
            LOGGER.error("Generic interruption error.");
        }
    }
}