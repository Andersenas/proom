package org.proom.server.services;

import com.github.f4b6a3.uuid.util.UuidValidator;
import org.junit.jupiter.api.Test;
import org.proom.engine.game.Board;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author vasyalike
 */
class BoardServiceTest {

    private final BoardService boardService = new BoardService(0);

    @Test
    public void testGetBoards() {
        assertEquals(1, boardService.getBoards().size());
        var board = boardService.getBoards().getFirst();
        assertNotNull(board.id());
        assertEquals(new BigDecimal("0.05"), board.smallBlind());
        assertEquals(new BigDecimal("0.10"), board.bigBlind());
        assertEquals(0, board.playerCount());
        assertEquals(Board.MAX_PLAYERS, board.maxPlayers());
    }

    @Test
    public void testGetEmptyBoard() {
        var boardId = boardId();
        var board = boardService.getBoard(boardId, null);
        assertEquals(boardId, board.id());
        assertEquals(BigDecimal.ZERO, board.pot());
        assertEquals(-1, board.buttonIndex());
        assertEquals(0, board.actionIndex());
        assertEquals(0, board.actionTimeLeft());
        assertEquals(emptyList(), board.communityCards());
        assertEquals(emptyList(), board.players());
    }

    private static final int POSITION_3 = 2;
    private static final int ALL_PLAYERS_COUNT = 3;

    @Test
    public void testGetPlayersBoard() {
        var boardId = boardId();
        boardService.sitIn(boardId, 0, "EID_1");
        var board = boardService.sitIn(boardId, 1, "EID_2");
        assertEquals(boardId, board.id());
        assertEquals(new BigDecimal("0.15"), board.pot());
        assertEquals(0, board.buttonIndex());
        assertEquals(0, board.actionIndex());
        assertEquals(0, board.actionTimeLeft());
        assertEquals(emptyList(), board.communityCards());
        assertEquals(
                List.of(
                        new PlayerState("EID_1", new BigDecimal("9.95"), new BigDecimal("0.05"), false),
                        new PlayerState("EID_2", new BigDecimal("9.90"), new BigDecimal("0.10"), false)
                ),
                board.players()
        );

        board = boardService.sitIn(boardId, POSITION_3, "EID_3");
        assertEquals(ALL_PLAYERS_COUNT, board.players().size());
        assertEquals(
                new PlayerState("EID_3", new BigDecimal("10.00"), BigDecimal.ZERO, true),
                board.players().getLast()
        );
        assertTrue(UuidValidator.isValid(board.player().playerId()));

        board = boardService.getBoard(boardId, board.player().playerId());
        assertTrue(UuidValidator.isValid(board.player().playerId()));
    }

    @Test
    public void testInvalidGetBoard() {
        var thrown = assertThrows(InvalidBoardException.class, () -> boardService.getBoard("INVALID_ID", null));
        assertEquals(Map.of("boardId", "INVALID_ID"), thrown.getData());
    }

    private static final int NEW_HAND_DELAY_SLEEP_MILLIS = 100;

    @Test
    public void testFold() throws Exception {
        var boardId = boardId();
        var playerId1 = boardService.sitIn(boardId, 0, "PLRID_1").player().playerId();
        var board = boardService.sitIn(boardId, 1, "PLRID_2");
        boardService.sitIn(boardId, 2, "PLRID_3");
        assertEquals(2, board.player().holeCards().size());
        var playerId2 = board.player().playerId();
        board = boardService.fold(boardId, playerId1);
        assertNotNull(board.player());
        assertEquals(
                List.of(new BigDecimal("9.95"), new BigDecimal("10.05"), new BigDecimal("10.00")),
                board.players().stream().map(PlayerState::chips).toList()
        );
        assertTrue(board.finished());
        var oldHandId = board.handId();
        Thread.sleep(NEW_HAND_DELAY_SLEEP_MILLIS);
        board = boardService.getBoard(boardId, null);
        assertNotEquals(oldHandId, board.handId());
        boardService.fold(boardId, playerId2);
        assertFalse(board.finished());
    }

    @Test
    public void testCall() {
        var boardId = boardId();
        var playerId = boardService.sitIn(boardId, 0, "PLRID_1").player().playerId();
        boardService.sitIn(boardId, 1, "PLRID_2");
        var board = boardService.call(boardId, playerId);
        assertNotNull(board.player());
        assertEquals(new BigDecimal("9.90"), board.players().getFirst().chips());
        assertEquals(new BigDecimal("9.90"), board.players().getLast().chips());
        assertEquals(1, board.actionIndex());
        assertFalse(board.finished());
    }

    @Test
    public void testCheck() {
        var boardId = boardId();
        var playerId1 = boardService.sitIn(boardId, 0, "PLRID_1").player().playerId();
        var playerId2 = boardService.sitIn(boardId, 1, "PLRID_2").player().playerId();
        boardService.call(boardId, playerId1);
        var board = boardService.check(boardId, playerId2);
        assertNotNull(board.player());
        assertEquals(new BigDecimal("9.90"), board.players().getFirst().chips());
        assertEquals(new BigDecimal("9.90"), board.players().getLast().chips());
        assertEquals(1, board.actionIndex());
        assertFalse(board.finished());
    }

    private String boardId() {
        return boardService.getBoards().getFirst().id();
    }
}
