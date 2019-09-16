package Base;

import Messages.AliveMessage;
import Networking.PeerNode;
import Messages.Message;
import Messages.UnknownMessageException;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Chambers are responsible for dispatching messages
 * and for informing legislators of joiners and leavers.
 */
public class Chamber implements Serializable {
    private final Logger LOGGER = LogManager.getLogger();

    private Set<UUID> members;
    private Set<Legislator> localMembers;
    private PeerNode network;

    public Chamber(PeerNode peer) {
        members = new HashSet<>();
        network = peer;
        localMembers = new HashSet<>();
    }

    public UUID enter(Legislator legislator) {
        localMembers.add(legislator);
        Pair<UUID, Set<UUID>> p = network.join();
        setMembers(p.getValue());
        return p.getKey();
    }

    public void requestDecree(Decree decree) {
        LOGGER.info("Requesting decree:" + decree);
        for(Legislator l: localMembers) {
            if (l.isPresident()) {
                l.requestDecree(decree);
                break;
            } else if (localMembers.size() == 1)
                LOGGER.info("Request denied, not the president.");
        }
    }

    public void dispatch(Message message) {
        network.enqueueOutput(message);
    }

    public void acquire(Message message) {
        boolean acquired = false;
        for(Legislator l: localMembers) {
            if(message.getRecipient().equals(l.getMemberID()) && // if the recipient is correct (i.e. current peer)
                    !message.getSender().equals(l.getMemberID())) { // and if is not a loopback message
                acquired = process(l, message);
            } else if(!acquired && ! message.getRecipient().equals(l.getMemberID()))
                LOGGER.warn("Wrong recipient message. Discarded!");
        }
    }

    private boolean process(Legislator l, Message m) {
        boolean acquired = false;
        try{
            if(m instanceof AliveMessage)
                updatePresident((AliveMessage) m);
            else
                l.read(m);

            acquired = true;
        } catch (UnknownMessageException e) {
            LOGGER.error("Discarding unknown message.");
        }
        return acquired;
    }

    private void updatePresident(AliveMessage m) {
        Legislator l = new ArrayList<>(getLocalMembers()).get(0);
        int result = l.getMemberID().compareTo(m.getSender());
        if(result <= 0) {
            l.setPresident(false);
        } else {
            l.setPresident(true);
        }
    }

    public void announce(Message message) {
        try {
            network.broadcast(message);
        } catch (Exception e) {
            LOGGER.error("Broadcast failed.");
        }
    }

    public Set<Legislator> getLocalMembers() {
        return localMembers;
    }

    public void setMembers(Set<UUID> members) {
        this.members = members;
    }

    public Set<UUID> getMembers() {
        return members;
    }
}
