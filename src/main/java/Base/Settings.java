package Base;

import MajorityStrategies.MajorityStrategy;
import MajorityStrategies.RandomMajority;

public class Settings {

    public static final int TRANSFER_TIME = 500;

    public static final int PROCESSING_TIME = 500;

    public static final int PORT = 1444; //Used for UDP P2P communication

    public static final MajorityStrategy majority = new RandomMajority();

    //public static final String TRACKER2 = "192.168.43.188";

    public static final String TRACKER2 = "192.168.43.154";
}
