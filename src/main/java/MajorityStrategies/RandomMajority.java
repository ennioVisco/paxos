package MajorityStrategies;

import Base.Legislator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RandomMajority implements MajorityStrategy {
    @Override
    public Set<UUID> selectMajoritySet(Set<UUID> ls) {
        return randomChoice(ls);
    }

    private Set<UUID> randomChoice(@NotNull Set<UUID> ls) {
        int i;
        int size = (ls.size() / 2) + 1;
        Set<UUID> quorum = new HashSet<>();
        for (i = 0; i < size; i++) {
            quorum.add(choice(ls));
        }
        return quorum;
    }

    private static <T> T choice(@NotNull Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for(T t: coll) if (--num < 0) return t;
        throw new AssertionError();
    }
}
