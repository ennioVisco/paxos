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

    private Tracker remoteObject;
    private Registry registry;

    public TrackerNode() {
        members = new HashMap<>();
        remoteObject = null;
    }

    public static void main(String[] args){
        TrackerNode node = new TrackerNode();
        node.run();
    }

    public void run() {
        System.setProperty("java.rmi.server.hostname",Settings.TRACKER2);
        LOGGER.info("Starting tracker at " + Settings.TRACKER2);
        try {
            TrackerNode obj = new TrackerNode();
            Tracker stub = (Tracker) UnicastRemoteObject.exportObject(obj, 0);
            remoteObject = stub;
            // Bind the remote object's stub in the registry
            registry = LocateRegistry.createRegistry(1099);
            registry.bind("PaxosTracker", stub);
            //Naming.rebind(Settings.TRACKER, new TrackerNode());
            LOGGER.info("Tracker ready.");
        } catch (Exception e) {
            LOGGER.error("Tracker exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public Tracker getRemoteObject() {
        return remoteObject;
    }

    public Registry getRegistry() {
        return registry;
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
