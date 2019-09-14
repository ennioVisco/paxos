package Base;

import MajorityStrategies.MajorityStrategy;
import MajorityStrategies.RandomMajority;

public class Settings {

    public static final float TRANSFER_TIME = 500;

    public static final float PROCESSING_TIME = 500;

    public static final int PORT = 1444; //Used for UDP P2P communication

    public static final MajorityStrategy majority = new RandomMajority();

    public static final String TRACKER = "pivi.myhop.tk";
}
