package Base;

import Messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Chamber {
    private Map<UUID,Legislator> members;

    public Chamber() {
        members = new HashMap<>();
    }

    public UUID addMember(Legislator legislator) {
        //TODO: should generate a unique ID
        UUID memberID = UUID.randomUUID();
        members.put(memberID, legislator);
        return memberID;
    }

    public Set<UUID> getMembers() {
        return members.keySet();
    }

    public void dispatch(Message message) {
        //TODO: send message over the network
    }

    public void broadcast(Message message) {
        for(Legislator l: members.values()) {
            message.setRecipient(l.getMemberID());
            dispatch(message);
        }
    }
}
