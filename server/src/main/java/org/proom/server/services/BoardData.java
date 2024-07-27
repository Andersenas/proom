package org.proom.server.services;

import org.proom.engine.game.Board;

import java.math.BigDecimal;

/**
 * @author vasyalike
 */
public record BoardData(
        String id,
        BigDecimal smallBlind,
        BigDecimal bigBlind,
        int maxPlayers,
        int playerCount
) {
    public BoardData(Board board) {
        this(
                board.getId(),
                board.getSmallBlind(),
                board.getBigBlind(),
                board.getMaxPlayers(),
                board.getPlayerCount()
        );
    }
}
