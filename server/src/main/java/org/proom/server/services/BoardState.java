package org.proom.server.services;

import org.proom.engine.cards.Card;
import org.proom.engine.game.Board;
import org.proom.engine.hand.Hand;
import org.proom.engine.hand.Player;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author vasyalike
 */
public record BoardState(
        String id,
        String handId,
        BigDecimal pot,
        int buttonIndex,
        int actionIndex,
        int actionTimeLeft,
        boolean finished,
        List<Card> communityCards,
        List<PlayerState> players,
        PlayerSecretState player
) {
    public BoardState(Board board, Player player) {
        this(board, board.getHand(), player);
    }

    public BoardState(Board board, Hand hand, Player player) {
        this(
                board.getId(),
                hand != null ? hand.getId() : null,
                hand != null ? hand.getPot() : BigDecimal.ZERO,
                board.getButtonIndex(),
                hand != null ? hand.getActionIndex() : 0,
                0,
                hand == null || hand.isFinished(),
                hand != null ? hand.getCommunityCards() : emptyList(),
                board.getPlayers().stream().map(PlayerState::new).toList(),
                player == null ? null : new PlayerSecretState(player)
        );
    }


}
