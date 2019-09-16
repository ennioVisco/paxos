package App;

import Base.Legislator;
import Networking.PeerNode;
import Networking.TrackerNode;
import Testing.Generics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Ignore
class FileSystemAppTest {
    private static final Logger LOGGER = LogManager.getLogger();
/*
    @Test
    void runServer() {
        FileSystemApp.main(new String[] {"-role","s"});
    }

    @Ignore("not yet ready , Please ignore.")
    @Test
    void runPeer() {
        FileSystemApp.main(null);
    }


*/

    @Test
    void run2() {
        TrackerNode tracker = new TrackerNode();
        Runnable runnable = tracker::run;
        Thread t = new Thread(runnable);
        t.start();
        Generics.trySleep(800);

        PeerNode p1 = new PeerNode();
        PeerNode p2 = new PeerNode();

        new Thread(p1).start();
        new Thread(p2).start();
        Generics.trySleep(20000);

        Legislator l1 = new ArrayList<>(p1.getChamber().getLocalMembers()).get(0);
        Legislator l2 = new ArrayList<>(p2.getChamber().getLocalMembers()).get(0);
        l1.requestDecree(new FileOperation("prova1"));
        l2.requestDecree(new FileOperation("prova2"));

        Generics.trySleep(11000);

        LOGGER.debug("First result:");
        Assert.assertEquals(2, p1.getChamber().getMembers().size());
        LOGGER.debug("Second result:");
        Assert.assertEquals(2, p2.getChamber().getMembers().size());
    }
}