package Networking;

import Base.Chamber;
import Base.Decree;
import Base.Legislator;
import Base.Settings;
import Messages.JoinedMessage;
import Messages.Message;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class PeerNode implements Runnable{
    private static final Logger LOGGER = LogManager.getLogger();

    private TransferQueue<UDPMessage> oQueue;
    private TransferQueue<Message> iQueue;

    private Tracker tracker;
    private Map<UUID, InetSocketAddress> peers;
    private InetSocketAddress address;
    private volatile boolean active;
    private boolean simulating = false;

    private Chamber chamber;

    public PeerNode() {
        init();
        this.chamber = new Chamber(this);
    }

    public PeerNode(boolean simulating) {
        this();
        this.simulating = simulating;
    }

    public PeerNode(Chamber chamber) {
        init();
        this.chamber = chamber;
        simulating = true;
    }

    private void init() {
        // Initialize queues
        oQueue = new LinkedTransferQueue<>();
        iQueue = new LinkedTransferQueue<>();
        active = true;
    }

    @Override
    public void run() {
        LOGGER.info("Starting Peer");

        // Setting up communication channels
        if(!simulating)
            initIOChannels();

        // Setting up Paxos starting state
        new Legislator(chamber);

        while(active) {
            try {
                Message m = iQueue.take();
                handleMessage(m);
            } catch (InterruptedException e) {
                LOGGER.fatal("Unknown error while catching messages.");
                e.printStackTrace();
            }

            if(!active)
                break;
        }
    }

    public void handleMessage(Message m) {
        LOGGER.info("Received a new message");
        if(m instanceof JoinedMessage) { //Refresh list of members
            JoinedMessage j = (JoinedMessage) m;
            peers = j.getMembers();
            chamber.setMembers(j.getMembers().keySet());
            LOGGER.info("Chamber members updated");
        } else
            chamber.acquire(m);
    }

    private void initIOChannels() {
        // Initialize threads
        InboundChannel inboundChannel = new InboundChannel(iQueue);
        OutboundChannel outboundChannel = new OutboundChannel(oQueue);
        Thread inputManager = new Thread(inboundChannel);
        Thread outputManager = new Thread(outboundChannel);

        // Record address
        address = (inboundChannel).getAddress();

        // Run threads
        inputManager.start();
        outputManager.start();

        // Now that communication channels are almost ready...
        // Contact tracker
        try {
            Registry registry = LocateRegistry.getRegistry(Settings.TRACKER);
            tracker = (Tracker) registry.lookup("PaxosTracker");
            LOGGER.info("Tracker found at " + Settings.TRACKER);
        } catch(NotBoundException | RemoteException | NullPointerException e) {
            LOGGER.fatal("Unable to contact the tracker! Silently continuing (will simulate later on)...");
            e.getMessage();
        }
    }

    public void localRequest(Decree decree) {
        LOGGER.info("Requesting decree:" + decree);
        chamber.requestDecree(decree);
    }

    public void shutdown() {
        LOGGER.info("Shutting down the peer.");
        active = false;
    }

    /**
     * join is a remote call to the tracker.
     * The tracker is supposed to return a Pair
     * @return a Pair (UUID, [UUID]) where
     *          - the first is the identifier of current legislator
     *          - the second is the list of all legislators currently active
     */
    public Pair<UUID, Set<UUID>> join() {
        try {
            //TODO: should pass UDP socket
            Pair<UUID, Map<UUID, InetSocketAddress>> status = tracker.join(address);
            peers = status.getValue();
            return new Pair<>(status.getKey(),peers.keySet());
        } catch (RemoteException | NullPointerException e) {
            LOGGER.error("Unable to enter the network... simulating locally!");
            return simulateTracker();

        }
    }

    /**
     * We will ignore received address, and use null to mark local peers.
     * Therefore we ask to chamber the list of members, add the one of our peer
     * and return the updated list.
     */
    private Pair<UUID, Set<UUID>> simulateTracker() {
        // We first add the current member to Chamber set
        UUID id = UUID.randomUUID();
        Set<UUID> members = chamber.getMembers();
        members.add(id);

        // Now we have to construct the peer map
        peers = new HashMap<>();
        for(UUID m : members) {
            peers.put(m, null);
        }
        // Finally, we return the updated set
        return new Pair<>(id, members);
    }

    public void broadcast(Message m) {
        for(UUID r : peers.keySet()) {
            m.setRecipient(r);
            enqueueOutput(m);
        }
    }

    public void enqueueOutput(Message m) {
        UUID r = m.getRecipient();
        InetSocketAddress d = peers.get(r);
        if(d != null) {
            LOGGER.debug("Message to <" + d + "> enqueued on outbound queue: " + m);
            oQueue.add(new UDPMessage(m,d));
        } else {
            LOGGER.debug("Message to local peer: " + m);
            handleMessage(m);
        }

    }

    public Chamber getChamber() {
        return chamber;
    }
}
