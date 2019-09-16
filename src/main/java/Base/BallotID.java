package Base;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class BallotID implements Comparable<BallotID>, Serializable {
    private Pair<Integer, UUID> id;

    public BallotID(Integer i, UUID l) {
        id = new Pair<>(i, l);
    }

    public Integer getKey() {
        return id.getKey();
    }

    public UUID getValue() {
        return id.getValue();
    }

    public void setKey(Integer key) {
        id = new Pair<>(key, id.getValue());
    }

    public void setValue(UUID value) {
        id = new Pair<>(id.getKey(), value);
    }

    @Override
    public int compareTo(@NotNull BallotID b) {
        int e = id.getKey().compareTo(b.getKey());
        if (e == 0)
            return id.getValue().compareTo(b.getValue());
        else
            return e;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(o instanceof BallotID) {
            BallotID b = (BallotID) o;
            return id.getKey() == b.getKey() && id.getValue() == b.getValue();
        }
        return false;
    }

    public String toString2() {
        return id.getKey().toString() + id.getValue().toString();
    }
}
