package Base;

import Messages.Message;
import Messages.UnknownMessageException;
import Networking.Tracker;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TransferQueue;

/**
 * Chambers are responsible for dispatching messages
 * and for informing legislators of joiners and leavers.
 */
public class Chamber {
    private final Logger LOGGER = LogManager.getLogger();

    private Map<UUID,InetAddress> members;
    private Legislator localMember;
    private Tracker tracker;
    private TransferQueue<Message> output;

    public Chamber(TransferQueue<Message> oQueue) {
        members = new HashMap<>();
        output = oQueue;
        try {
            Registry registry = LocateRegistry.getRegistry(Settings.TRACKER);
            tracker = (Tracker) registry.lookup("PaxosTracker");
        } catch(Exception e) {
            LOGGER.error("Unable to contact the tracker... simulating locally!");
        }
    }

    public UUID join(Legislator legislator) {
        localMember = legislator;
        try {
            Pair<UUID, Map<UUID, InetAddress>> status = tracker.join(InetAddress.getLocalHost());
            members = status.getValue();
            return status.getKey();
        } catch (Exception e) {
            LOGGER.error("Unable to join the network... simulating locally!");
            UUID id = UUID.randomUUID();
            try {
                members.put(id, InetAddress.getLocalHost());
            } catch (Exception e1) {
                LOGGER.fatal("Unable to simulate");
            }
            return id;
        }
    }

    public Map<UUID,InetAddress> getMembers() {
        return members;
    }

    public void dispatch(Message message) {
        output.add(message);
        LOGGER.debug("Enqueued message " + message);
    }

    public void deliver(Message message) {
        if(message.getRecipient() == localMember.getMemberID() &&
                message.getSender() != localMember.getMemberID()) {
            try{
                localMember.receive(message);
            } catch (UnknownMessageException e) {
                LOGGER.error("Discarding unknown message.");
            }
        } else
            LOGGER.warn("Wrong recipient message discarded.");
    }

    public void broadcast(Message message) {
        try {
            tracker.broadcast(message);
        } catch (Exception e) {
            LOGGER.error("Broadcast failed.");
        }
    }
}
