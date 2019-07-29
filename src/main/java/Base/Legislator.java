package Base;

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
    private List<Decree> decrees;
    private UUID memberID;

    private Ledger ledger;

    private Set<UUID> quorum;
    private Ballot currentBallot;

    public Legislator(@NotNull Chamber chamber) {
        this.memberID = chamber.join(this);
        this.ledger = new Ledger(this);
        this.chamber = chamber;
        this.decrees = new ArrayList<>();

        //TODO: this should adapt to leavers
        quorum = Settings.majority.selectMajoritySet(chamber.getMembers().keySet());
    }

    /**
     * Dispatching strategy for messages received.
     * @param m new message received
     */
    public Message receive(Message m) throws UnknownMessageException {
        if(m instanceof NextBallotMessage) {
            NextBallotMessage nb = (NextBallotMessage) m;
            return lastVote(nb.getSender(), nb.getBallot());
        } else if (m instanceof LastVoteMessage) {
            LastVoteMessage lv = (LastVoteMessage) m;
            return processLastVote(lv.getSender(), lv.getBound(), lv.getVote());
        } else if (m instanceof BeginBallotMessage) {
            BeginBallotMessage bb = (BeginBallotMessage) m;
            return vote(bb.getSender(), bb.getBallot());
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
    public BallotID nextBallot() {
        BallotID ballotID = ledger.getNewBallotId();
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
    private Message lastVote(UUID r, BallotID bound) {
        if(bound.compareTo(ledger.getNextBallotID()) > 0)
            ledger.setNextBallot(bound);
        else
            bound = ledger.getNextBallotID();

        Pair<BallotID, Vote> message = new Pair<>(bound, ledger.getPreviousVote());
        Message m = new LastVoteMessage(r, message, this);
        send(m);
        return m;
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
    private Message processLastVote(UUID s, BallotID b, Vote v) {
        if(ledger.getLastTriedBallot().equals(b)) {
            Set<Vote> lvr = ledger.getLastVotesReceived();
            if (quorum.contains(s) && !lvr.contains(v))
                ledger.addLastVoteReceived(v);
            if (lvr.size() == quorum.size())
                return beginBallot(b);
        } else if(b.compareTo(ledger.getLastTriedBallot()) > 0) {
            ledger.setNextBallot(b);
            nextBallot();
            return null;
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
     * @return the Voted message sent or a LastVote message in case the ballot ID is less than nextBallotID.
     */
    private Message vote(UUID r, Ballot b) {
        //TODO: should be able to choose not to vote
        if(b.getBallotID().compareTo(ledger.getNextBallotID()) >= 0) {
            Vote v = new Vote(this, b, b.getDecree());
            ledger.addPreviousVote(v);
            Message m = new VotedMessage(r, v, this);
            send(m);
            return m;
        }
        return lastVote(r, b.getBallotID());
    }

    /**
     * Activated on receiving a Voted message.
     * If everyone in the quorum has voted, a Success message is broadcasted.
     * Algorithm step 5.
     * @param v Vote received from an agreeing legislator.
     * @return the Success message sent or null.
     */
    private Message processVote(Vote v) {
        if(v.getBallot().getBallotID().equals(ledger.getLastTriedBallot())) {
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

    private Message beginBallot(BallotID b) {
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
            if(v.getBallot().getBallotID().compareTo(maxVote.getBallot().getBallotID()) > 0)
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
