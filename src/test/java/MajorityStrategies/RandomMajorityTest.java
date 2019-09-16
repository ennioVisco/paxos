package MajorityStrategies;

import Base.Chamber;
import Networking.PeerNode;
import Testing.Generics;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

class RandomMajorityTest {

    private Set<UUID> ls;

    @BeforeEach
    void setUp() {
        PeerNode p1 = new PeerNode(true);
        Chamber c = p1.getChamber();

        new Thread(p1).start();
        Generics.trySleep(50);


        for(int i = 0; i < 4; i++) {
            new Thread(new PeerNode(c)).start();
            Generics.trySleep(50);
        }

        ls = c.getMembers();
    }

    @Test
    void selectMajoritySet() {
        MajorityStrategy rule = new RandomMajority();
        Set<UUID> quorum = rule.selectMajoritySet(ls);
        Assert.assertEquals(3, quorum.size());
    }
}