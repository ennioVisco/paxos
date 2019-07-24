package Base;

import Messages.Message;

import java.util.Map;
import java.util.Set;

public class Chamber {
    private Map<Integer,Legislator> members;



    public Integer addMember(Legislator legislator) {
        //TODO: should generate a unique ID
        Integer memberID = 1;
        members.put(memberID, legislator);
        return memberID;
    }

    public Set<Integer> getMembers() {
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
