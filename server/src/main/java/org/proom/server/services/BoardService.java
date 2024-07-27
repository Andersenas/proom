package org.proom.server.services;

import org.proom.engine.game.Board;
import org.proom.engine.hand.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.proom.server.services.BoardChecker.checkBoard;

/**
 * @author vasyalike
 */
@Service
public final class BoardService {

    private final ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private final int newHandDelaySec;

    private final Board board;

    public BoardService(@Value("${new_hand_delay_sec}") int newHandDelaySec) {
        this.newHandDelaySec = newHandDelaySec;
        board = new Board();
    }

    public List<BoardData> getBoards() {
        return List.of(new BoardData(board));
    }

    public BoardState getBoard(String boardId, String playerId) {
        checkBoard(board, boardId);

        return new BoardState(board, board.getPlayer(playerId));
    }

    public BoardState sitIn(String boardId, int position, String externalId) {
        checkBoard(board, boardId);

        var player = board.sitIn(new Player(externalId), position);
        checkNewHand(true);
        return new BoardState(board, player);
    }

    public BoardState fold(String boardId, String playerId) {
        checkBoard(board, boardId);

        var player = board.getHand().fold(playerId);
        return boardStateBeforeNewHand(player);
    }

    public BoardState call(String boardId, String playerId) {
        checkBoard(board, boardId);

        var player = board.getHand().call(playerId);
        return boardStateBeforeNewHand(player);
    }

    public BoardState check(String boardId, String playerId) {
        checkBoard(board, boardId);

        var player = board.getHand().check(playerId);
        return boardStateBeforeNewHand(player);
    }

    private BoardState boardStateBeforeNewHand(Player player) {
        var res = new BoardState(board, player);
        checkNewHand();
        return res;
    }

    private void checkNewHand() {
        checkNewHand(false);
    }

    private void checkNewHand(boolean instant) {
        if (board.canStartNewHand()) {
            if (instant) {
                board.newHand();
            } else {
                scheduledExecutor.schedule(board::newHand, newHandDelaySec, SECONDS);
            }
        }
    }
}
