package org.proom.server.services;

import org.proom.engine.cards.Card;
import org.proom.engine.hand.Player;

import java.util.List;

/**
 * @author vasyalike
 */
public record PlayerSecretState(String playerId, List<Card> holeCards) {
    public PlayerSecretState(Player player) {
        this(player.getPlayerId(), player.getHoleCards());
    }
}
