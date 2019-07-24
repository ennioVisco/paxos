package Base;

import MajorityStrategies.MajorityStrategy;
import MajorityStrategies.RandomMajority;
import Messages.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Legislator {
    private static final Logger LOGGER = LogManager.getLogger();

    // External interfaces
    private Chamber chamber;
    private MajorityStrategy majorityRule;
    private List<Decree> decrees;
    private UUID memberID;


    private Ledger ledger;

    private Set<Ballot> pendingBallots;
    private Set<Vote> lastVotesBallot;


    private Set<UUID> quorum;
    private Ballot currentBallot;
    private int ballotID;

    //Unique ballot numbers as <ballotID, legislatorID>

    public Legislator(Chamber chamber, MajorityStrategy majorityRule) {
        this.memberID = chamber.addMember(this);
        this.chamber = chamber;
        this.majorityRule = majorityRule;
        this.decrees = new ArrayList<>();
        this.ledger = new Ledger();

        //TODO: this should adapt to leavers
        quorum = majorityRule.selectMajoritySet(chamber.getMembers());
    }

    public void nextBallot() {
        ballotID = ledger.getNewBallotId();
        for (UUID r : quorum)
            send(new NextBallotMessage(r, ballotID, this));
    }

    private void lastVote(UUID r, Integer bound) {
        Vote v = ledger.getLastVote(bound);
        Pair<Integer, Vote> message = new Pair<>(bound, v);
        ledger.addVoteMessage(new LastVoteMessage(r, message, this));
        send(new LastVoteMessage(r, message, this));
    }

    //called on receive of LastVote()
    private void processLastVote(UUID s, Vote v, Integer b) {
        if (quorum.contains(s) && !lastVotesBallot.contains(v))
            lastVotesBallot.add(v);
        if (lastVotesBallot.size() == quorum.size())
            beginBallot(b);
    }

    //TODO: should be able to choose not to vote
    private void vote(UUID r, Ballot b) {
        Vote v = new Vote(this, b, b.getDecree());
        send(new VotedMessage(r, v, this));
    }

    private void processVote(Vote v) {
        currentBallot.addVote(v);
        if(currentBallot.getVotes().size() == currentBallot.getQuorum().size()){
            sendAll(new SuccessMessage(currentBallot, this));
            currentBallot = null;
        }
    }

    private void updateLedger(Ballot b) {
        ledger.addApprovedBallot(b);
    }

    private void beginBallot(Integer b) {
        Decree d = chooseDecree();
        currentBallot = new Ballot(b, d, quorum);

        for(UUID l : quorum) {
            send(new BeginBallotMessage(l, currentBallot, this));
        }
    }

    private Decree chooseDecree() {
        //TODO: verify to satisfy B3
        Decree decree = maxVoteDecree();
        if(decree == BasicDecrees.BLANK_DECREE)
            return decrees.get(1);
        return decree;
    }

    private Decree maxVoteDecree() {
        Vote maxVote = Vote.NullVote(this);
        for(Vote v : lastVotesBallot) {
            if(v.getBallot().getBallotID() > maxVote.getBallot().getBallotID())
                maxVote = v;
        }
        return maxVote.getDecree();
    }

    public void receive(Message m) {
        if(m instanceof NextBallotMessage) {
            NextBallotMessage nb = (NextBallotMessage) m;
            lastVote(nb.getRecipient(), nb.getBallot());
        } else if (m instanceof LastVoteMessage) {
            LastVoteMessage lv = (LastVoteMessage) m;
            processLastVote(lv.getRecipient(), lv.getVote(), lv.getBound());
        } else if (m instanceof BeginBallotMessage) {
            BeginBallotMessage bb = (BeginBallotMessage) m;
            vote(bb.getRecipient(), bb.getBallot());
        } else if (m instanceof VotedMessage) {
            VotedMessage vm = (VotedMessage) m;
            processVote(vm.getVote());
        } else if (m instanceof SuccessMessage) {
            SuccessMessage sm = (SuccessMessage) m;
            updateLedger(sm.getBallot());
        }
    }

    public Legislator(Chamber chamber) {
        this(chamber, new RandomMajority());
    }

    public void send(Message m) {
        chamber.dispatch(m);
    }

    public void sendAll(Message m) {
        chamber.broadcast(m);
    }

    public Legislator() {
        this(new Chamber());
    }

    public UUID getMemberID() {
        return memberID;
    }

    public void requestDecree(Decree d) {
        decrees.add(d);
    }
}
