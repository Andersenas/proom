package org.proom.engine.hand;

import org.proom.engine.cards.Card;
import org.proom.engine.exceptions.InvalidActionPlayerException;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.unmodifiableList;

/**
 * @author vasyalike
 */
final class HandPlayers {
    private static final int HEADS_UP_COUNT = 2;

    private final List<Player> players;
    private final int buttonIndex;
    private int actionIndex;

    HandPlayers(List<Player> players, int buttonIndex) {
        this.players = players;
        this.buttonIndex = buttonIndex;
        this.actionIndex = withRotation(getBigBlindIndex() + 1);
    }

    int withRotation(int index) {
        return index % players.size();
    }

    void setHoleCards(List<List<Card>> holeCards) {
        for (var i = 0; i < holeCards.size(); i++) {
            players.get(withRotation(i + buttonIndex + 1)).setHoleCards(unmodifiableList(holeCards.get(i)));
        }
    }

    int size() {
        return players.size();
    }

    private int getSmallBlindIndex() {
        return withRotation(buttonIndex + (isHeadsUp() ? 0 : 1));
    }

    private int getBigBlindIndex() {
        return withRotation(getSmallBlindIndex() + 1);
    }

    private boolean isHeadsUp() {
        return size() == HEADS_UP_COUNT;
    }

    int getActionIndex() {
        return actionIndex;
    }

    void nextAction() {
        actionIndex = withRotation(actionIndex + 1);
    }

    void setPreFlopActionIndex() {
        actionIndex = withRotation(getBigBlindIndex() + 1);
    }

    void setPostFlopActionIndex() {
        actionIndex = isHeadsUp() ? getBigBlindIndex() : getSmallBlindIndex();
    }

    List<Player> getNonFoldPlayers() {
        return players.stream().filter(Predicate.not(Player::isFold)).toList();
    }

    List<Player> getActivePlayers() {
        return players.stream().filter(Predicate.not(Player::isFold)).filter(Player::hasChips).toList();
    }

    List<Player> getPotPlayers() {
        return getActivePlayers().isEmpty() ? getNonFoldPlayers() : getActivePlayers();
    }

    boolean isAtMostOneActivePlayer() {
        return getActivePlayers().size() <= 1;
    }

    void postBlinds(BigDecimal smallBlind) {
        players.get(getSmallBlindIndex()).bet(smallBlind, false);
        players.get(getBigBlindIndex()).bet(smallBlind.multiply(BigDecimal.TWO), false);
    }

    boolean allActed() {
        return players.stream().allMatch(p -> p.isActed() || !p.hasChips());
    }

    void resetActedPlayers(Player player) {
        for (var p : players) {
            if (!p.isFold()
                    && !p.getPlayerId().equals(player.getPlayerId())
                    && p.getChips().compareTo(BigDecimal.ZERO) != 0
            ) {
                p.setActed(false);
            }
        }
    }

    Player getActivePlayer(String playerId) {
        var res = players.get(actionIndex);
        if (!res.getPlayerId().equals(playerId)) {
            throw new InvalidActionPlayerException(res.getPlayerId(), playerId);
        }
        return res;
    }

    void resetNextRound() {
        players.forEach(Player::resetNextRound);
    }
}
