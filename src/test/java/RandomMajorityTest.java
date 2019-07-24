import Base.Legislator;
import MajorityStrategies.MajorityStrategy;
import MajorityStrategies.RandomMajority;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class RandomMajorityTest {
    private Legislator l1;
    private Legislator l2;
    private Legislator l3;
    private Legislator l4;
    private Legislator l5;
    private Set<UUID> ls;

    @BeforeEach
    void setUp() {
        l1 = new Legislator();
        l2 = new Legislator();
        l3 = new Legislator();
        l4 = new Legislator();
        l5 = new Legislator();
        ls = new HashSet<>();
        ls.add(l1.getMemberID());
        ls.add(l2.getMemberID());
        ls.add(l3.getMemberID());
        ls.add(l4.getMemberID());
        ls.add(l5.getMemberID());
    }

    @Test
    void selectMajoritySet() {
        MajorityStrategy rule = new RandomMajority();
        Set<UUID> quorum = rule.selectMajoritySet(ls);
        Assert.assertEquals(3, quorum.size());
    }
}