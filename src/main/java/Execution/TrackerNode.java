package Execution;

import Base.Settings;
import Messages.Message;
import Networking.Peer;
import Networking.Tracker;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackerNode implements Tracker {
    private static final Logger LOGGER = LogManager.getLogger();

    private Map<UUID, InetAddress> members;

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
        LOGGER.info("Tracker exiting.");
    }

    private void dispatch(Message message, Map.Entry<UUID, InetAddress> legislator) throws RemoteException {
        try {
            Peer.send(message, legislator.getValue());
        } catch(Exception e) {
            LOGGER.error("Failed to contact " + legislator.getKey());
        }
    }

    public Map<UUID, InetAddress> getMembers() {
        return members;
    }

    @Override
    public Pair <UUID, Map<UUID, InetAddress>> join(InetAddress location) throws RemoteException {
        UUID memberID = UUID.randomUUID();
        members.put(memberID, location);
        LOGGER.info("New peer joined: <" + memberID + "," + location.toString() + ">");
        Pair <UUID, Map<UUID, InetAddress>> data = new Pair<>(memberID, members);
        return data;
    }

    @Override
    public void broadcast(Message message) throws RemoteException {
        for(Map.Entry<UUID, InetAddress> l: members.entrySet()) {
            message.setRecipient(l.getKey());
            dispatch(message, l);
        }
    }
}
