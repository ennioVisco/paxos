package Networking;

import Messages.Message;
import javafx.util.Pair;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public interface Tracker extends Remote {

    Pair<UUID, Map<UUID, InetAddress>> join(InetAddress location) throws RemoteException;

    void broadcast(Message message) throws RemoteException;

}
