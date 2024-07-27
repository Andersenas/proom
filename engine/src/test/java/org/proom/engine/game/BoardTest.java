package org.proom.engine.game;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.util.UuidUtil;
import org.junit.jupiter.api.Test;
import org.proom.engine.exceptions.ChosenSeatException;
import org.proom.engine.exceptions.DuplicatePlayerException;
import org.proom.engine.exceptions.InvalidPositionException;
import org.proom.engine.exceptions.InvalidStartHandException;
import org.proom.engine.exceptions.MaxPlayersException;
import org.proom.engine.hand.Player;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.IntStream;

import static com.github.f4b6a3.uuid.enums.UuidVersion.VERSION_TIME_ORDERED_EPOCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.proom.engine.game.Board.SMALL_BLIND;

/**
 * @author vasyalike
 */
public class BoardTest {

    private static final String ID = "eid";

    private final Board board = new Board();

    @Test
    public void testMaxPlayers() {
        var player = new Player(ID);
        board.sitIn(player, 0);
        assertEquals(1, board.getPlayerCount());
        assertFalse(board.isFull());

        IntStream.range(1, board.getMaxPlayers()).forEach(i -> board.sitIn(new Player(ID + i), i));
        assertEquals(board.getMaxPlayers(), board.getPlayerCount());
        assertTrue(board.isFull());

        var thrown = assertThrows(MaxPlayersException.class, () -> board.sitIn(new Player(ID + "-1"), 0));
        assertEquals(
                Map.of("externalId", ID + "-1", "totalPlayers", board.getMaxPlayers()),
                thrown.getData()
        );
    }

    @Test
    public void testDistinctPlayers() {
        var player = new Player(ID);
        board.sitIn(player, 0);
        var thrown = assertThrows(DuplicatePlayerException.class, () -> board.sitIn(player, 1));
        assertEquals(Map.of("externalId", ID), thrown.getData());
    }

    @Test
    public void testNewHandAndButtonRotate() {
        var player = board.sitIn(new Player(ID), 0);
        assertFalse(board.canStartNewHand());
        var thrown = assertThrows(InvalidStartHandException.class, board::newHand);
        assertEquals(Map.of("boardId", board.getId()), thrown.getData());
        var player2 = board.sitIn(new Player(ID + 1), 1);
        assertTrue(board.canStartNewHand());
        assertEquals(2, board.getPlayers().size());
        assertTrue(board.getPlayers().stream().allMatch(Player::isWaiting));

        board.newHand();
        assertFalse(board.canStartNewHand());
        assertEquals(0, board.getButtonIndex());
        board.getHand().fold(player.getPlayerId());
        assertTrue(board.canStartNewHand());
        board.newHand();
        assertEquals(1, board.getButtonIndex());
        assertFalse(board.canStartNewHand());
        board.getHand().fold(player2.getPlayerId());
        assertTrue(board.canStartNewHand());
        board.newHand();
        assertEquals(0, board.getButtonIndex());
        assertEquals(2, board.getPlayers().size());
        assertTrue(board.getPlayers().stream().noneMatch(Player::isWaiting));

        assertNull(board.getPlayer(null));
        assertNull(board.getPlayer("INVALID_PLRID"));
        assertEquals(player.getPlayerId(), board.getPlayer(player.getPlayerId()).getPlayerId());
    }

    @Test
    public void testDefault() {
        assertEquals(VERSION_TIME_ORDERED_EPOCH, UuidUtil.getVersion(UuidCreator.fromString(board.getId())));
        assertEquals(SMALL_BLIND, board.getSmallBlind());
        assertEquals(SMALL_BLIND.multiply(BigDecimal.TWO), board.getBigBlind());
        assertNull(board.getHand());
        assertFalse(board.canStartNewHand());
    }

    @Test
    public void testChosenSeatException() {
        var player1 = new Player(ID);
        board.sitIn(player1, 2);
        var player2 = new Player(ID + 1);
        var thrown = assertThrows(ChosenSeatException.class, () -> board.sitIn(player2, 2));
        assertEquals(
                Map.of("externalId", ID + 1, "position", 2),
                thrown.getData()
        );
    }

    @Test
    public void testPositionException() {
        var thrown = assertThrows(InvalidPositionException.class, () -> board.sitIn(new Player(ID), -1));
        assertEquals(
                Map.of("externalId", ID, "invalidPosition", -1),
                thrown.getData()
        );
        thrown = assertThrows(InvalidPositionException.class, () -> board.sitIn(new Player(ID), board.getMaxPlayers()));
        assertEquals(
                Map.of("externalId", ID, "invalidPosition", board.getMaxPlayers()),
                thrown.getData()
        );
    }

    @Test
    public void testCorrectSeat() {
        var player3 = new Player(ID);
        var player2 = new Player(ID + 1);
        var player1 = new Player(ID + 0);
        board.sitIn(player3, 2);
        board.sitIn(player2, 1);
        board.sitIn(player1, 0);
        board.newHand();
        assertEquals(new BigDecimal("10.00"), player1.getChips());
        assertEquals(new BigDecimal("9.95"), player2.getChips());
        assertEquals(new BigDecimal("9.90"), player3.getChips());
    }
}
