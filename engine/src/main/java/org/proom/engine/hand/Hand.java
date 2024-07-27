package org.proom.engine.hand;

import com.github.f4b6a3.uuid.alt.GUID;
import org.proom.engine.cards.Card;
import org.proom.engine.cards.Deck;
import org.proom.engine.exceptions.FinishedHandException;
import org.proom.engine.exceptions.InvalidAllInException;
import org.proom.engine.exceptions.InvalidCallException;
import org.proom.engine.exceptions.InvalidCheckException;
import org.proom.engine.exceptions.MinimumRaiseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static org.proom.engine.hand.HandState.FINISHED;
import static org.proom.engine.hand.HandState.PRE_FLOP;

/**
 * @author vasyalike
 */
public final class Hand {

    private final String id;
    private final Deck deck;
    private final BigDecimal smallBlind;
    private final BigDecimal bigBlind;
    private final List<Card> communityCards = new ArrayList<>();

    private final HandPlayers handPlayers;

    private HandState state;

    private BigDecimal callAmount;
    private BigDecimal raiseAmount;
    private BigDecimal pot;
    private final Map<HandState, List<AllIn>> allIns;

    public Hand(List<Player> players, int buttonIndex, BigDecimal smallBlind) {
        this(players, buttonIndex, smallBlind, new Deck());
    }

    Hand(List<Player> players, int buttonIndex, BigDecimal smallBlind, Deck deck) {
        this.id = GUID.v7().toString();
        this.handPlayers = new HandPlayers(new ArrayList<>(players), buttonIndex);
        this.smallBlind = smallBlind;
        this.bigBlind = smallBlind.multiply(BigDecimal.TWO);
        this.deck = deck;
        state = null;
        pot = new BigDecimal(0);
        allIns = new HashMap<>();

        postBlinds();
        handPlayers.setHoleCards(deck.dealHoleCards(handPlayers.size()));
        nextRound();
    }

    private void postBlinds() {
        handPlayers.postBlinds(smallBlind);
        addToPot(smallBlind.add(bigBlind));
        callAmount = bigBlind;
        raiseAmount = BigDecimal.ZERO;
    }

    private void addToPot(BigDecimal amount) {
        pot = pot.add(amount);
    }

    public String getId() {
        return id;
    }

    public boolean isFinished() {
        return state == FINISHED;
    }

    public BigDecimal getPot() {
        return pot;
    }

    HandState getState() {
        return state;
    }

    public List<Card> getCommunityCards() {
        return unmodifiableList(communityCards);
    }

    public int getActionIndex() {
        return handPlayers.getActionIndex();
    }

    public Player fold(String playerId) {
        var player = getActivePlayer(playerId);
        player.setFold();
        var actingPlayers = handPlayers.getNonFoldPlayers();
        if (actingPlayers.size() == 1) {
            actingPlayers.getFirst().setActed(true);
        }
        nextAction();
        return player;
    }

    public Player call(String playerId) {
        var player = getActivePlayer(playerId);
        var prevBetAmount = player.getBetAmount();
        var amount = callAmount.subtract(player.getBetAmount());
        player.bet(amount);
        if (allIns.containsKey(getState())) {
            allIns.get(getState()).add(new AllIn(player.getBetAmount(), player));
            addToPot(prevBetAmount.negate());
        } else {
            addToPot(amount);
        }
        nextAction();
        return player;
    }

    public Player check(String playerId) {
        var player = getActivePlayer(playerId);
        if (callAmount.compareTo(BigDecimal.ZERO) != 0 && callAmount.compareTo(player.getBetAmount()) != 0) {
            throw new InvalidCheckException(playerId, callAmount.subtract(player.getBetAmount()));
        }
        player.setCheck(true);
        nextAction();
        return player;
    }

    public void raise(String playerId, BigDecimal amount) {
        var player = getActivePlayer(playerId);
        if (player.getChips().subtract(amount).compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidAllInException(playerId);
        }
        if (amount.add(player.getBetAmount()).compareTo(callAmount) <= 0) {
            throw new InvalidCallException(playerId);
        }
        if (amount.compareTo(callAmount.add(getRaiseAmount())) < 0) {
            throw new MinimumRaiseException(callAmount.add(getRaiseAmount()), amount, playerId);
        }
        bet(player, amount);
        nextAction();
    }

    public void allIn(String playerId) {
        var player = getActivePlayer(playerId);
        var amount = player.getChips();
        var allInAmount = player.getAllInAmount();
        allIns.computeIfAbsent(getState(), _ -> new ArrayList<>()).add(new AllIn(allInAmount, player));
        bet(player, amount);
        pot = pot.subtract(allInAmount);
        nextAction();
    }

    private void bet(Player player, BigDecimal amount) {
        var totalBetAmount = amount.add(player.getBetAmount());
        player.bet(amount);
        if (player.getBetAmount().compareTo(callAmount) > 0) {
            handPlayers.resetActedPlayers(player);
        }
        addToPot(amount);
        raiseAmount = totalBetAmount.subtract(callAmount);
        callAmount = totalBetAmount;
    }

    private void nextAction() {
        handPlayers.nextAction();
        if (handPlayers.allActed()) {
            if (handPlayers.isAtMostOneActivePlayer()) {
                while (state != FINISHED) {
                    nextRound();
                }
            } else {
                nextRound();
            }
        }
    }

    private void nextRound() {
        state = HandState.next(state);
        switch (state) {
            case FLOP -> communityCards.addAll(deck.burnAndDealFlop());
            case TURN, RIVER -> communityCards.addAll(deck.burnAndDealSingle());
            case FINISHED -> showdown();
            default -> { }
        }
        bettingRound();
    }

    private void bettingRound() {
        if (state == PRE_FLOP) {
            handPlayers.setPreFlopActionIndex();
        } else if (state != FINISHED) {
            handPlayers.setPostFlopActionIndex();
            handPlayers.resetNextRound();
            callAmount = BigDecimal.ZERO;
            raiseAmount = BigDecimal.ZERO;
        }
    }

    private void showdown() {
        var potWinners = findSidePots();
        potWinners.add(new PotWinners(getPot(), new WinnerFinder(handPlayers.getPotPlayers(), communityCards).apply()));
        for (var potWinner : potWinners) {
            awardPot(potWinner.pot(), potWinner.players());
        }
        state = FINISHED;
    }

    private List<PotWinners> findSidePots() {
        var res = new ArrayList<PotWinners>();
        for (var allIners : allIns.values()) {
            var betAmount = allIners.stream().min(comparing(AllIn::amount)).orElseThrow().amount();
            for (var allIn : allIners) {
                if (allIn.amount().compareTo(betAmount) > 0) {
                    allIn.player().awardPot(allIn.amount().subtract(betAmount));
                }
            }
            var sidePot = betAmount.multiply(BigDecimal.valueOf(allIners.size()));
            var winners = new WinnerFinder(allIners.stream().map(AllIn::player).toList(), communityCards).apply();
            res.add(new PotWinners(sidePot, winners));
        }
        return res;
    }

    private void awardPot(BigDecimal pot, List<Player> players) {
        var splitPot = pot.divide(new BigDecimal(players.size()), RoundingMode.HALF_EVEN);
        players.forEach(p -> p.awardPot(splitPot));
    }

    private Player getActivePlayer(String playerId) {
        if (state == FINISHED) {
            throw new FinishedHandException(playerId);
        }
        return handPlayers.getActivePlayer(playerId);
    }

    private BigDecimal getRaiseAmount() {
        return raiseAmount.compareTo(BigDecimal.ZERO) == 0 ? bigBlind : raiseAmount;
    }
}
