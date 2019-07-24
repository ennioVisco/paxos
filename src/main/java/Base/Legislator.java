package Base;

import Messages.LastVoteMessage;

import java.util.*;

public class Legislator {
    // External interfaces
    private Set<Legislator> chamber;
    private MajorityStrategy majorityRule;
    private List<Decree> decrees;

    //Algorithm interfaces
    private Map<Integer,Vote> previousVotes;
    private List<LastVoteMessage> lastVoteMessages;

    //Unique ballot numbers as <ballotID, legislatorID>

    public Legislator(Set<Legislator> chamber, MajorityStrategy majorityRule) {
        chamber.add(this);
        this.chamber = chamber;
        this.majorityRule = majorityRule;
        this.decrees = new ArrayList<>();
        this.previousVotes = new HashMap<>();
    }

    public void nextBallot() {
        Set<Legislator> quorum = majoritySet(chamber);
        int bid = chooseBallotId();
        Decree decree = chooseDecree(quorum, bid);
    }

    public Ballot beginBallot(Integer b, Decree d, Set<Legislator> q) {
        //send
        return new Ballot(b, d, q);
    }

    private Decree chooseDecree(Set<Legislator> quorum, Integer b) {
        Decree decree = maxVoteDecree(quorum, b);
        if(decree == BasicDecrees.BLANK_DECREE)
            return decrees.get(1);
        return decree;
    }

    private Decree maxVoteDecree(Set<Legislator> quorum, Integer b) {
        Vote maxVote = Vote.NullVote(this);
        for(Legislator l : quorum) {
            Vote v = sendNextBallot(l, b);
            if(v.getBallot().getBallotID() > maxVote.getBallot().getBallotID())
                maxVote = v;
        }
        return maxVote.getDecree();
    }

    private Vote sendNextBallot(Legislator l, Integer b) {
        //TODO: this should happen over the network
        return l.sendLastVote(this, b);
    }

    private Vote sendLastVote(Legislator r, Integer bid) {
        //TODO: instead of returning should send over network
        Vote bind = Vote.NullVote(this);
        for(Integer b: previousVotes.keySet()){
            if(b < bid && bind.getBallot().getBallotID() < b) {
                bind = previousVotes.get(b);
            }
        }
        lastVoteMessages.add(new LastVoteMessage(r, bind));
        return bind;
    }

    private int chooseBallotId() {
        //TODO: must satisfy B1 rule
        return Collections.max(previousVotes.keySet());
    }

    public Legislator(HashSet<Legislator> chamber) {
        //TODO: change MajoritySet rule and Chamber implementation
        this(chamber, new RandomMajority());
    }

    public Legislator() {
        this(new HashSet<>());
    }

    private Set<Legislator> majoritySet(Set<Legislator> ls) {
        return majorityRule.selectMajoritySet(ls);
    }

    public void requestDecree(Decree d) {
        decrees.add(d);
    }
}
