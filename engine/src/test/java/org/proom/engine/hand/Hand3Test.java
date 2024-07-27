package org.proom.engine.hand;

import com.github.f4b6a3.uuid.util.UuidValidator;
import org.junit.jupiter.api.Test;
import org.proom.engine.exceptions.InvalidActionPlayerException;
import org.proom.engine.exceptions.InvalidCallException;
import org.proom.engine.exceptions.MinimumRaiseException;
import org.proom.engine.exceptions.NotEnoughChipsToBet;
import org.proom.engine.exceptions.InvalidAllInException;
import org.proom.engine.exceptions.InvalidCheckException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.proom.engine.hand.HandState.FINISHED;
import static org.proom.engine.hand.HandState.FLOP;
import static org.proom.engine.hand.HandState.RIVER;
import static org.proom.engine.hand.HandState.TURN;
import static org.proom.engine.scaffold.TestDecks.threePlayersDealerWinDeck;
import static org.proom.engine.scaffold.TestHands.BIG_BLIND;
import static org.proom.engine.scaffold.TestHands.SMALL_BLIND;
import static org.proom.engine.scaffold.TestHands.hand3Players;

/**
 * @author vasyalike
 */
class Hand3Test {

    @Test
    public void testBettingRoundInvalidPlayer() throws Exception {
        var h3p = hand3Players(0);
        assertTrue(UuidValidator.isValid(h3p.hand().getId()));
        assertFalse(h3p.hand().isFinished());

        var thrown = assertThrows(
                InvalidActionPlayerException.class,
                () -> h3p.hand().raise(h3p.player2().getPlayerId(), BigDecimal.ONE)
        );
        assertEquals(
                Map.of(
                        "currentPlayerId", h3p.player1().getPlayerId(),
                        "invalidPlayerId", h3p.player2().getPlayerId()
                ),
                thrown.getData()
        );
    }

    @Test
    public void testBettingRound() throws Exception {
        var h3p = hand3Players(0);
        assertEquals(0, h3p.hand().getActionIndex());
        h3p.hand().raise(h3p.player1().getPlayerId(), BigDecimal.TWO);
        assertEquals(new BigDecimal("8.00"), h3p.player1().getChips());
        assertEquals(new BigDecimal("2.15"), h3p.hand().getPot());
        assertEquals(1, h3p.hand().getActionIndex());
    }

    @Test
    public void testBettingRoundMinRaise() throws Exception {
        var h3p = hand3Players(0);
        var thrown = assertThrows(
                MinimumRaiseException.class,
                () -> h3p.hand().raise(h3p.player1().getPlayerId(), new BigDecimal("0.11"))
        );
        assertEquals(
                Map.of(
                        "playerId", h3p.player1().getPlayerId(),
                        "betAmount", new BigDecimal("0.11"),
                        "minBet", new BigDecimal("0.20")
                ),
                thrown.getData()
        );
    }

    @Test
    public void testFlopBettingRound() throws Exception {
        var h3p = hand3Players(0);

        var thrown = assertThrows(
                InvalidActionPlayerException.class,
                () -> h3p.hand().raise(h3p.player2().getPlayerId(), BigDecimal.ONE)
        );
        assertEquals(
                Map.of(
                        "currentPlayerId", h3p.player1().getPlayerId(),
                        "invalidPlayerId", h3p.player2().getPlayerId()
                ),
                thrown.getData()
        );
    }

    @Test
    public void test3PlayersBBFold() throws Exception {
        var h3p = hand3Players(0);
        h3p.hand().call(h3p.player1().getPlayerId());
        h3p.hand().call(h3p.player2().getPlayerId());
        h3p.hand().fold(h3p.player3().getPlayerId());
        assertEquals(FLOP, h3p.hand().getState());
    }

    @Test
    public void test3Players1Fold() throws Exception {
        var h3p = hand3Players(0);
        h3p.hand().fold(h3p.player1().getPlayerId());
        assertTrue(h3p.player1().isFold());
        assertNotEquals(FINISHED, h3p.hand().getState());
        assertEquals(1, h3p.hand().getActionIndex());
    }

    @Test
    public void testCheck() throws Exception {
        var h3p = hand3Players(0);
        h3p.hand().call(h3p.player1().getPlayerId());
        h3p.hand().call(h3p.player2().getPlayerId());
        h3p.hand().check(h3p.player3().getPlayerId());
        assertEquals(FLOP, h3p.hand().getState());
        assertFalse(h3p.player3().isCheck());
        assertNotEquals(TURN, h3p.hand().getState());
        assertEquals(h3p.hand().getActionIndex(), 1);
        var checkPlayers = List.of(h3p.player2(), h3p.player3(), h3p.player1());
        checkPlayers.forEach(p -> h3p.hand().check(p.getPlayerId()));
        assertNotEquals(FLOP, h3p.hand().getState());
        assertEquals(TURN, h3p.hand().getState());
        assertNotEquals(RIVER, h3p.hand().getState());
        checkPlayers.forEach(p -> h3p.hand().check(p.getPlayerId()));
        assertEquals(RIVER, h3p.hand().getState());
    }

    @Test
    public void testInvalidRaiseInsteadOfCall() throws Exception {
        var h3p = hand3Players(0);

        var thrown = assertThrows(
                InvalidCallException.class,
                () -> h3p.hand().raise(h3p.player1().getPlayerId(), BIG_BLIND)
        );
        assertEquals(Map.of("playerId", h3p.player1().getPlayerId()), thrown.getData());
    }

    @Test
    public void testRaiseAndCall() throws Exception {
        var h3p = hand3Players(0);
        h3p.hand().raise(h3p.player1().getPlayerId(), new BigDecimal("0.20"));
        h3p.hand().call(h3p.player2().getPlayerId());
        h3p.hand().call(h3p.player3().getPlayerId());
        assertEquals(FLOP, h3p.hand().getState());
        assertNotEquals(TURN, h3p.hand().getState());
        assertEquals(h3p.hand().getActionIndex(), 1);
    }

    @Test
    public void testInvalidCheck() throws Exception {
        var h3p = hand3Players(0);
        var thrown = assertThrows(InvalidCheckException.class, () -> h3p.hand().check(h3p.player1().getPlayerId()));
        assertEquals(
                Map.of("playerId", h3p.player1().getPlayerId(), "toBet", BIG_BLIND),
                thrown.getData()
        );
    }

    @Test
    public void testThreePlayersAllInFold() throws Exception {
        var h3p = hand3Players(0, threePlayersDealerWinDeck());
        h3p.hand().fold(h3p.player1().getPlayerId());
        h3p.hand().allIn(h3p.player2().getPlayerId());
        h3p.hand().call(h3p.player3().getPlayerId());
        assertEquals(FINISHED, h3p.hand().getState());
        assertTrue(h3p.hand().isFinished());
        assertEquals(0, new BigDecimal("10.00").compareTo(h3p.player1().getChips()));
        assertEquals(new BigDecimal("20.00"), h3p.player2().getChips());
        assertEquals(0, h3p.player3().getChips().intValueExact());
    }

    @Test
    public void testNotEnoughChipsToRaise() throws Exception {
        var h3p = hand3Players(0);
        var thrown = assertThrows(
                NotEnoughChipsToBet.class,
                () -> h3p.hand().raise(h3p.player1().getPlayerId(), new BigDecimal("10.10"))
        );
        assertEquals(
                Map.of(
                        "playerId", h3p.player1().getPlayerId(),
                        "chips", new BigDecimal("10.00"),
                        "betAmount", new BigDecimal("10.10")
                ),
                thrown.getData()
        );
    }

    @Test
    public void testCall() throws Exception {
        var h3p = hand3Players(0, threePlayersDealerWinDeck());
        h3p.hand().call(h3p.player1().getPlayerId());
        h3p.hand().allIn(h3p.player2().getPlayerId());
        h3p.hand().call(h3p.player3().getPlayerId());
        h3p.hand().call(h3p.player1().getPlayerId());
        assertEquals(FINISHED, h3p.hand().getState());
        assertEquals(new BigDecimal("30.00"), h3p.player1().getChips());
        assertEquals(0, h3p.player2().getChips().intValueExact());
        assertEquals(0, h3p.player3().getChips().intValueExact());
    }

    private static final int FIVE_BB_RAISE = 5;
    private static final int EIGHT_BB_RE_RAISE = 8;
    @Test
    public void testReRaise() throws Exception {
        var h3p = hand3Players(0, threePlayersDealerWinDeck());
        h3p.hand().raise(h3p.player1().getPlayerId(), BIG_BLIND.multiply(BigDecimal.TWO));
        h3p.hand().raise(
                h3p.player2().getPlayerId(),
                BIG_BLIND.multiply(new BigDecimal(FIVE_BB_RAISE)).subtract(SMALL_BLIND)
        );

        var thrown = assertThrows(
                MinimumRaiseException.class,
                () -> h3p.hand().raise(h3p.player3().getPlayerId(), new BigDecimal("0.56"))
        );
        assertEquals(
                Map.of(
                        "playerId", h3p.player3().getPlayerId(),
                        "betAmount", new BigDecimal("0.56"),
                        "minBet", BIG_BLIND.multiply(new BigDecimal(EIGHT_BB_RE_RAISE))
                ),
                thrown.getData()
        );

        h3p.hand().raise(h3p.player3().getPlayerId(), new BigDecimal("1.50"));
        h3p.hand().call(h3p.player1().getPlayerId());
        h3p.hand().raise(h3p.player2().getPlayerId(), new BigDecimal("2.70"));
        h3p.hand().call(h3p.player3().getPlayerId());
        h3p.hand().call(h3p.player1().getPlayerId());
        assertEquals(FLOP, h3p.hand().getState());
        assertEquals(new BigDecimal("3.20").multiply(new BigDecimal("3")), h3p.hand().getPot());
    }

    @Test
    public void testInvalidAllIn() throws Exception {
        var h3p = hand3Players(0);
        var thrown = assertThrows(
                InvalidAllInException.class,
                () -> h3p.hand().raise(h3p.player1().getPlayerId(), h3p.player1().getChips())
        );
        assertEquals(Map.of("playerId", h3p.player1().getPlayerId()), thrown.getData());
    }

    @Test
    public void test3PlayersMinAllIn() throws Exception {
        var winner = new Player("id1", new BigDecimal("5.00"));
        var player1 = new Player("id2", new BigDecimal("10.00"));
        var player2 = new Player("id3", new BigDecimal("10.00"));
        var hand = new Hand(List.of(winner, player1, player2), 0, SMALL_BLIND, threePlayersDealerWinDeck());
        hand.allIn(winner.getPlayerId());
        hand.call(player1.getPlayerId());
        hand.call(player2.getPlayerId());
        assertEquals(FLOP, hand.getState());
        hand.raise(player1.getPlayerId(), BIG_BLIND);
        hand.call(player2.getPlayerId());
        assertEquals(TURN, hand.getState());
        hand.check(player1.getPlayerId());
        hand.check(player2.getPlayerId());
        assertEquals(RIVER, hand.getState());
        hand.check(player1.getPlayerId());
        hand.check(player2.getPlayerId());
        assertEquals(FINISHED, hand.getState());
        assertEquals(new BigDecimal("15.00"), winner.getChips());
        assertEquals(new BigDecimal("5.10"), player1.getChips());
        assertEquals(new BigDecimal("4.90"), player2.getChips());
    }

    @Test
    public void test3Players2AllIns() throws Exception {
        var winner1 = new Player("id1", new BigDecimal("5.00"));
        var winner2 = new Player("id2", new BigDecimal("10.00"));
        var looser = new Player("id3", new BigDecimal("15.00"));
        var hand = new Hand(List.of(winner1, winner2, looser), 0, SMALL_BLIND, threePlayersDealerWinDeck());
        hand.allIn(winner1.getPlayerId());
        hand.call(winner2.getPlayerId());
        hand.call(looser.getPlayerId());
        assertEquals(FLOP, hand.getState());
        hand.allIn(winner2.getPlayerId());
        hand.call(looser.getPlayerId());
        assertEquals(FINISHED, hand.getState());
        assertEquals(new BigDecimal("15.00"), winner1.getChips());
        assertEquals(new BigDecimal("10.00"), winner2.getChips());
        assertEquals(new BigDecimal("5.00"), looser.getChips());
    }
}
