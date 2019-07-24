package MajorityStrategies;

import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface MajorityStrategy {

    @Contract(pure = true)
    Set<Integer> selectMajoritySet(Set<Integer> ls);
}
