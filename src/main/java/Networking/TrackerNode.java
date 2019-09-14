package Networking;

import Base.Settings;
import Messages.JoinedMessage;
import Messages.Message;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackerNode implements Tracker {
    private static final Logger LOGGER = LogManager.getLogger();

    private Map<UUID, InetSocketAddress> members;

    public TrackerNode() {
        members = new HashMap<>();
    }

    public static void main(String[] args){
        TrackerNode node = new TrackerNode();
        node.run();
    }

    public void run() {
        System.setProperty("java.rmi.server.hostname","192.168.1.122");
        LOGGER.info("Starting tracker at " + Settings.TRACKER);
        try {
            TrackerNode obj = new TrackerNode();
            Tracker stub = (Tracker) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("PaxosTracker", stub);
            //Naming.rebind(Settings.TRACKER, new TrackerNode());
            LOGGER.info("Tracker ready.");
        } catch (Exception e) {
            LOGGER.error("Tracker exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void dispatch(Message message, InetSocketAddress legislator) throws RemoteException {
        try {
            OutboundChannel.send(message, legislator);
        } catch(Exception e) {
            LOGGER.error("Failed to contact " + legislator);
        }
    }

    @Override
    public Pair <UUID, Map<UUID, InetSocketAddress>> join(InetSocketAddress location) throws RemoteException {
        UUID memberID = UUID.randomUUID();
        members.put(memberID, location);
        LOGGER.info("New peer joined: <" + memberID + "," + location + ">");
        Pair <UUID, Map<UUID, InetSocketAddress>> data = new Pair<>(memberID, members);
        broadcast(new JoinedMessage(memberID, members, null));
        return data;
    }

    @Override
    public void broadcast(Message message) throws RemoteException {
        for(Map.Entry<UUID, InetSocketAddress> l: members.entrySet()) {
            message.setRecipient(l.getKey());
            dispatch(message, l.getValue());
        }
    }

    public Map<UUID, InetSocketAddress> getMembers() {
        return members;
    }
}
