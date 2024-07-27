package org.proom.engine.hand;

import org.junit.jupiter.api.Test;
import org.proom.engine.exceptions.FinishedHandException;
import org.proom.engine.exceptions.InvalidActionPlayerException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.proom.engine.hand.HandState.FINISHED;
import static org.proom.engine.hand.HandState.FLOP;
import static org.proom.engine.hand.HandState.RIVER;
import static org.proom.engine.hand.HandState.TURN;
import static org.proom.engine.scaffold.TestDecks.headsUpDealerLooseDeck;
import static org.proom.engine.scaffold.TestDecks.headsUpSplitPotDeck;
import static org.proom.engine.scaffold.TestHands.BIG_BLIND;
import static org.proom.engine.scaffold.TestHands.SMALL_BLIND;
import static org.proom.engine.scaffold.TestHands.hand2Players;

/**
 * @author vasyalike
 */
public class HeadsUpHandTest {

    @Test
    public void testCheckShowDown() throws Exception {
        var h2p = hand2Players(0, headsUpDealerLooseDeck());
        h2p.hand().call(h2p.player1().getPlayerId());
        var player = h2p.hand().check(h2p.player2().getPlayerId());
        assertEquals(h2p.player2().getPlayerId(), player.getPlayerId());
        assertEquals(FLOP, h2p.hand().getState());
        Runnable checks = () -> {
            h2p.hand().check(h2p.player2().getPlayerId());
            h2p.hand().check(h2p.player1().getPlayerId());
        };
        checks.run();
        assertEquals(TURN, h2p.hand().getState());
        checks.run();
        assertEquals(RIVER, h2p.hand().getState());
        checks.run();
        assertEquals(FINISHED, h2p.hand().getState());
        assertThrows(FinishedHandException.class, () -> h2p.hand().check(h2p.player2().getPlayerId()));

        assertEquals(new BigDecimal("9.90"), h2p.player1().getChips());
        assertEquals(new BigDecimal("10.10"), h2p.player2().getChips());

        var h2p2 = hand2Players(1, headsUpDealerLooseDeck());
        h2p2.hand().call(h2p2.player2().getPlayerId());
        h2p2.hand().check(h2p2.player1().getPlayerId());
        checks = () -> {
            h2p2.hand().check(h2p2.player1().getPlayerId());
            h2p2.hand().check(h2p2.player2().getPlayerId());
        };
        checks.run();
        checks.run();
        checks.run();
        assertEquals(FINISHED, h2p2.hand().getState());
        assertThrows(FinishedHandException.class, () -> h2p2.hand().check(h2p2.player1().getPlayerId()));

        assertEquals(new BigDecimal("10.10"), h2p2.player1().getChips());
        assertEquals(new BigDecimal("9.90"), h2p2.player2().getChips());
    }

    @Test
    public void testAllInCall() throws Exception {
        var h2p = hand2Players(0, headsUpDealerLooseDeck());
        h2p.hand().allIn(h2p.player1().getPlayerId());
        var player = h2p.hand().call(h2p.player2().getPlayerId());
        assertEquals(h2p.player2().getPlayerId(), player.getPlayerId());
        assertEquals(FINISHED, h2p.hand().getState());
        assertEquals(0, h2p.player1().getChips().intValueExact());
        assertEquals(new BigDecimal("20.00"), h2p.player2().getChips());
    }

    @Test
    public void testAllInFold() throws Exception {
        var h2p = hand2Players(0, headsUpDealerLooseDeck());
        h2p.hand().allIn(h2p.player1().getPlayerId());
        var player = h2p.hand().fold(h2p.player2().getPlayerId());
        assertEquals(h2p.player2().getPlayerId(), player.getPlayerId());
        assertEquals(FINISHED, h2p.hand().getState());
        assertEquals(new BigDecimal("10.10"), h2p.player1().getChips());
        assertEquals(new BigDecimal("9.90"), h2p.player2().getChips());
    }

    @Test
    public void testSplitPot() throws Exception {
        var h2p = hand2Players(0, headsUpSplitPotDeck());
        h2p.hand().call(h2p.player1().getPlayerId());
        h2p.hand().check(h2p.player2().getPlayerId());
        Runnable checks = () -> {
            h2p.hand().check(h2p.player2().getPlayerId());
            h2p.hand().check(h2p.player1().getPlayerId());
        };
        checks.run();
        checks.run();
        checks.run();
        assertEquals(FINISHED, h2p.hand().getState());
        assertEquals(new BigDecimal("10.00"), h2p.player1().getChips());
        assertEquals(new BigDecimal("10.00"), h2p.player2().getChips());
    }

    @Test
    public void testHeadsUpFold() throws Exception {
        var h2p = hand2Players(0);
        h2p.hand().fold(h2p.player1().getPlayerId());
        assertTrue(h2p.player1().isFold());
        assertEquals(FINISHED, h2p.hand().getState());
        assertEquals(new BigDecimal("10.05"), h2p.player2().getChips());
    }

    @Test
    public void testAllInRestoreChips() throws Exception {
        var looser = new Player("eidX", new BigDecimal("20.00"));
        var player1 = new Player("eid1", new BigDecimal("10.00"));
        var hand = new Hand(List.of(looser, player1), 0, SMALL_BLIND, headsUpDealerLooseDeck());
        hand.call(looser.getPlayerId());
        hand.allIn(player1.getPlayerId());
        hand.allIn(looser.getPlayerId());
        assertEquals(FINISHED, hand.getState());
        assertEquals(new BigDecimal("20.00"), player1.getChips());
        assertEquals(new BigDecimal("10.00"), looser.getChips());
    }

    @Test
    public void testAllInNotMatch() throws Exception {
        var winner = new Player("eidX", new BigDecimal("20.00"));
        var player1 = new Player("eid1", new BigDecimal("10.00"));
        var hand = new Hand(List.of(player1, winner), 0, SMALL_BLIND, headsUpDealerLooseDeck());
        hand.call(player1.getPlayerId());
        hand.allIn(winner.getPlayerId());
        hand.allIn(player1.getPlayerId());
        assertEquals(FINISHED, hand.getState());
        assertEquals(0, player1.getChips().intValueExact());
        assertEquals(new BigDecimal("30.00"), winner.getChips());
    }

    @Test
    public void testPostFlopBigBlindFirstRaise() throws Exception {
        var h2p = hand2Players(1);
        h2p.hand().call(h2p.player2().getPlayerId());
        assertNotEquals(FLOP, h2p.hand().getState());
        h2p.hand().check(h2p.player1().getPlayerId());
        assertEquals(FLOP, h2p.hand().getState());
        h2p.hand().raise(h2p.player1().getPlayerId(), BIG_BLIND);
        assertEquals(new BigDecimal("0.30"), h2p.hand().getPot());
    }

    @Test
    public void testInvalidActionFold() throws Exception {
        var h2p = hand2Players(1);

        var thrown = assertThrows(
                InvalidActionPlayerException.class,
                () -> h2p.hand().fold(h2p.player1().getPlayerId())
        );
        assertEquals(
                Map.of(
                        "currentPlayerId", h2p.player2().getPlayerId(),
                        "invalidPlayerId", h2p.player1().getPlayerId()
                ),
                thrown.getData()
        );
        assertNotEquals(FINISHED, h2p.hand().getState());
    }
}
