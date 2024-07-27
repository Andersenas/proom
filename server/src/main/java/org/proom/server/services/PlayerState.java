package org.proom.server.services;

import org.proom.engine.hand.Player;

import java.math.BigDecimal;

/**
 * @author vasyalike
 */
public record PlayerState(String externalId, BigDecimal chips, BigDecimal bet, boolean waiting) {
    public PlayerState(Player player) {
        this(player.getExternalId(), player.getChips(), player.getBetAmount(), player.isWaiting());
    }
}
