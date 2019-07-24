package Base;

import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface MajorityStrategy {

    @Contract(pure = true)
    Set<Legislator> selectMajoritySet(Set<Legislator> ls);
}
