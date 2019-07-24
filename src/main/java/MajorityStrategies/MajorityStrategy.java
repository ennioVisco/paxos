package MajorityStrategies;

import org.jetbrains.annotations.Contract;

import java.util.Set;
import java.util.UUID;

public interface MajorityStrategy {

    @Contract(pure = true)
    Set<UUID> selectMajoritySet(Set<UUID> ls);
}
