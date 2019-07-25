import Base.Chamber;
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

    private Set<UUID> ls;

    @BeforeEach
    void setUp() {
        Chamber c = new Chamber();
        Legislator l1 = new Legislator(c);
        Legislator l2 = new Legislator(c);
        Legislator l3 = new Legislator(c);
        Legislator l4 = new Legislator(c);
        Legislator l5 = new Legislator(c);
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