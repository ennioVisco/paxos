package Networking.Runnables;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedAction {
    private Runnable action;
    private int period;
    private TimeUnit unit;

    public TimedAction(Runnable action, int milliseconds) {
        this(action, milliseconds, TimeUnit.MILLISECONDS);
    }

    public TimedAction(Runnable action, int period, TimeUnit unit) {
        this.action = action;
        this.period = period;
        this.unit = unit;

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(action, 0, period, unit);
    }

    public int getPeriod() {
        return period;
    }

    public Runnable getAction() {
        return action;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
