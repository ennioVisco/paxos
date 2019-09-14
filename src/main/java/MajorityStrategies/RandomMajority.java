package MajorityStrategies;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomMajority implements MajorityStrategy {
    @Override
    public Set<UUID> selectMajoritySet(Set<UUID> ls) {
        return randomChoice(ls);
    }

    private Set<UUID> randomChoice(@NotNull Set<UUID> ls) {
        int size = (ls.size() / 2) + 1;
        return new HashSet<>(pickNRandomElements(new ArrayList<>(ls),size));
    }

    private <E> List<E> pickNRandomElements(List<E> list, int n, Random r) {
        int length = list.size();

        if (length < n) return null;

        //We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - n; --i)
        {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    private <E> List<E> pickNRandomElements(List<E> list, int n) {
        return pickNRandomElements(list, n, ThreadLocalRandom.current());
    }
}
