package Networking;

import Messages.Message;
import javafx.util.Pair;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public interface Tracker extends Remote {

    Pair<UUID, Map<UUID, InetSocketAddress>> join(InetSocketAddress location) throws RemoteException;

    void broadcast(Message message) throws RemoteException;

}
