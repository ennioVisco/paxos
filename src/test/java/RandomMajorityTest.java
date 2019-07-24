import Base.Legislator;
import Base.MajorityStrategy;
import Base.RandomMajority;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class RandomMajorityTest {
    private Legislator l1;
    private Legislator l2;
    private Legislator l3;
    private Legislator l4;
    private Legislator l5;
    Set<Legislator> ls;

    @BeforeEach
    void setUp() {
        l1 = new Legislator();
        l2 = new Legislator();
        l3 = new Legislator();
        l4 = new Legislator();
        l5 = new Legislator();
        ls = new HashSet<>();
        ls.add(l1);
        ls.add(l2);
        ls.add(l3);
        ls.add(l4);
        ls.add(l5);
    }

    @Test
    void selectMajoritySet() {
        MajorityStrategy rule = new RandomMajority();
        Set<Legislator> quorum = rule.selectMajoritySet(ls);
        Assert.assertEquals(3, quorum.size());
    }
}