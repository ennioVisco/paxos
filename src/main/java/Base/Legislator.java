package Base;

import MajorityStrategies.MajorityStrategy;
import MajorityStrategies.RandomMajority;
import Messages.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This is the main class that collects legislators actions and knowledge.
 * Most actions are triggered on receive of a message from other legislators.
 * Any legislator is bound to a Chamber.
 */
public class Legislator {
    private static final Logger LOGGER = LogManager.getLogger();

    // External interfaces
    private Chamber chamber;
    private MajorityStrategy majorityRule;
    private List<Decree> decrees;
    private UUID memberID;

    private Ledger ledger;

    private Set<UUID> quorum;
    private Ballot currentBallot;

    public Legislator(@NotNull Chamber chamber, @NotNull MajorityStrategy majorityRule) {
        this.memberID = chamber.addMember(this);
        this.ledger = new Ledger(this);
        this.chamber = chamber;
        this.majorityRule = majorityRule;
        this.decrees = new ArrayList<>();

        //TODO: this should adapt to leavers
        quorum = majorityRule.selectMajoritySet(chamber.getMembers());
    }
    public Legislator(Chamber chamber) {
        this(chamber, new RandomMajority());
    }
    public Legislator() {
        this(new Chamber());
    }

    /**
     * Dispatching strategy for messages received.
     * @param m new message received
     */
    public Message receive(Message m) throws UnknownMessageException {
        if(m instanceof NextBallotMessage) {
            NextBallotMessage nb = (NextBallotMessage) m;
            return lastVote(nb.getRecipient(), nb.getBallot());
        } else if (m instanceof LastVoteMessage) {
            LastVoteMessage lv = (LastVoteMessage) m;
            return processLastVote(lv.getRecipient(), lv.getBound(), lv.getVote());
        } else if (m instanceof BeginBallotMessage) {
            BeginBallotMessage bb = (BeginBallotMessage) m;
            return vote(bb.getRecipient(), bb.getBallot());
        } else if (m instanceof VotedMessage) {
            VotedMessage vm = (VotedMessage) m;
            return processVote(vm.getVote());
        } else if (m instanceof SuccessMessage) {
            SuccessMessage sm = (SuccessMessage) m;
            return updateLedger(sm.getBallot());
        }
        throw new UnknownMessageException(m);
    }

    /**
     * Chamber wrapper for sending a 1-to-1 message.
     * @param m message to be sent
     */
    public void send(Message m) {
        chamber.dispatch(m);
    }

    /**
     * Chamber wrapper for sending a 1-to-all message.
     * @param m message to be sent.
     */
    public void sendAll(Message m) {
        chamber.broadcast(m);
    }

    /**
     * Entry point to start a new ballot.
     * Algorithm step 1.
     * @return the tentative new ballot ID.
     */
    public int nextBallot() {
        int ballotID = ledger.getNewBallotId();
        ledger.emptyLastVotesReceived();
        for (UUID r : quorum)
            send(new NextBallotMessage(r, ballotID, this));
        return ballotID;
    }

    /**
     * Activated on receiving a NextBallot message.
     * Sends a LastVote message.
     * Algorithm step 2.
     * @param r Ballot requester ID.
     * @param bound Tentative ballot ID to be checked.
     * @return the LastVote message sent.
     */
    private Message lastVote(UUID r, Integer bound) {
        if(bound > ledger.getNextBallotID()) {
            ledger.setNextBallot(bound);
            Pair<Integer, Vote> message = new Pair<>(bound, ledger.getPreviousVote());
            Message m = new LastVoteMessage(r, message, this);
            send(m);
            return m;
        }
        LOGGER.info("Ignoring the NextBallot just received with Bound:" + bound);
        return null;
    }

    /**
     * Activated on receiving a LastVote message.
     * If everybody replied, starts a new ballot and sends a BeginBallot message.
     * Algorithm step 3.
     * @param s ID of the sender.
     * @param b The ballot ID previously requested to satisfy.
     * @param v The last vote the sender expressed, satisfying the requested condition.
     * @return the BeginBallot message sent or null.
     */
    private Message processLastVote(UUID s, Integer b, Vote v) {
        if(b == ledger.getLastTriedBallot()) {
            Set<Vote> lvr = ledger.getLastVotesReceived();
            if (quorum.contains(s) && !lvr.contains(v))
                ledger.addLastVoteReceived(v);
            if (lvr.size() == quorum.size())
                return beginBallot(b);
        }
        LOGGER.info("Ignoring the LastVoteMessage just received with <Bound,Vote>:" +
                    "<" + b + "," + v.toString() + ">");
        return null;
    }

    /**
     * Activated on receiving a BeginBallot message.
     * At this stage, we decide whether to vote.
     * If we want to, a Voted message is sent.
     * Algorithm step 4.
     * @param r ID of the legislator to reply to in case of vote.
     * @param b The proposed ballot.
     * @return the Voted message sent.
     */
    private Message vote(UUID r, Ballot b) {
        //TODO: should be able to choose not to vote
        if(b.getBallotID() == ledger.getNextBallotID()) {
            Vote v = new Vote(this, b, b.getDecree());
            ledger.addVote(v);
            Message m = new VotedMessage(r, v, this);
            send(m);
            return m;
        }
        LOGGER.info("Ignoring the LastVoteMessage just received with Ballot:" + b.toString());
        return null;
    }

    /**
     * Activated on receiving a Voted message.
     * If everyone in the quorum has voted, a Success message is broadcasted.
     * Algorithm step 5.
     * @param v Vote received from an agreeing legislator.
     * @return the Success message sent or null.
     */
    private Message processVote(Vote v) {
        if(v.getBallot().getBallotID() == ledger.getLastTriedBallot()) {
            currentBallot.addVote(v);
            if (currentBallot.getVotes().size() == currentBallot.getQuorum().size()) {
                Message m = new SuccessMessage(currentBallot, this);
                sendAll(m);
                currentBallot = null;
                return m;
            }
        }
        LOGGER.info("Ignoring the Voted message just received with Vote:" + v.toString());
        return null;
    }

    /**
     * Activated on receiving a Success message.
     * It updates the ledger with the new decree that has now become law.
     * Algorithm step 6.
     * @param b The successful ballot.
     * @return a copy of the Success message received.
     */
    private Message updateLedger(Ballot b) {
        if(!decrees.isEmpty())
            if(b.getDecree() == decrees.get(1))
                decrees.remove(1);
        ledger.addApprovedBallot(b);
        return new SuccessMessage(b, memberID);
    }

    /*INTERNAL METHODS*/

    private Message beginBallot(Integer b) {
        Decree d = chooseDecree();
        currentBallot = new Ballot(b, d, quorum);
        Message m = null;
        for(UUID l : quorum) {
            m = new BeginBallotMessage(l, currentBallot, this);
            send(m);
        }
        return m;
    }

    private Decree chooseDecree() {
        //TODO: verify to satisfy B3
        Vote maxVote = Vote.NullVote(this);
        for(Vote v : ledger.getLastVotesReceived()) {
            if(v.getBallot().getBallotID() > maxVote.getBallot().getBallotID())
                maxVote = v;
        }

        if(maxVote.getDecree() == BasicDecrees.BLANK_DECREE && !decrees.isEmpty())
            return decrees.get(1);
        else if (maxVote.getDecree() == BasicDecrees.BLANK_DECREE)
            return BasicDecrees.TRIVIAL_DECREE;
        return maxVote.getDecree();
    }

    /* END OF INTERNAL METHODS */

    public UUID getMemberID() {
        return memberID;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void requestDecree(Decree d) {
        decrees.add(d);
    }
}
