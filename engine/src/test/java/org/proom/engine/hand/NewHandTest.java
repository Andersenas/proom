package org.proom.engine.hand;

import org.junit.jupiter.api.Test;
import org.proom.engine.cards.Deck;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.proom.engine.scaffold.TestHands.hand2Players;
import static org.proom.engine.scaffold.TestHands.hand3Players;

/**
 * @author vasyalike
 */
public class NewHandTest {

    private static final int BURN_AND_DEAL = 2;
    private static final int FLOP_SIZE = 3;
    private static final int DECK_SIZE_FLOP = 52 - 6 - FLOP_SIZE - 1;

    @Test
    public void testHeadsUpPostBlinds() throws Exception {
        var h2p = hand2Players(0);
        assertEquals(new BigDecimal("0.15"), h2p.hand().getPot());
        assertEquals(new BigDecimal("9.95"), h2p.player1().getChips());
        assertEquals(new BigDecimal("9.90"), h2p.player2().getChips());
        assertEquals(2, h2p.player1().getHoleCards().size());
        assertEquals(2, h2p.player2().getHoleCards().size());
    }

    @Test
    public void testButton0IndexPostBlindsAndHoleCards() throws Exception {
        var h3p = hand3Players(0);
        assertEquals(2, h3p.player1().getHoleCards().size());
        assertEquals(2, h3p.player2().getHoleCards().size());
        assertEquals(2, h3p.player3().getHoleCards().size());
        assertEquals(new BigDecimal("0.15"), h3p.hand().getPot());
        assertEquals(new BigDecimal("10.00"), h3p.player1().getChips());
        assertEquals(new BigDecimal("9.95"), h3p.player2().getChips());
        assertEquals(new BigDecimal("9.90"), h3p.player3().getChips());
    }

    @Test
    public void testButton1IndexPostBlindsAndHoleCards() throws Exception {
        var h3p = hand3Players(1);
        assertEquals(new BigDecimal("0.15"), h3p.hand().getPot());
        assertEquals(new BigDecimal("9.90"), h3p.player1().getChips());
        assertEquals(new BigDecimal("10.00"), h3p.player2().getChips());
        assertEquals(new BigDecimal("9.95"), h3p.player3().getChips());
    }

    @Test
    public void testButton2IndexPostBlindsAndHoleCards() throws Exception {
        var h3p = hand3Players(2);
        assertEquals(new BigDecimal("0.15"), h3p.hand().getPot());
        assertEquals(new BigDecimal("9.95"), h3p.player1().getChips());
        assertEquals(new BigDecimal("9.90"), h3p.player2().getChips());
        assertEquals(new BigDecimal("10.00"), h3p.player3().getChips());
    }

    @Test
    public void testDealCommunityCards() throws Exception {
        var h3p = hand3Players(0);
        h3p.hand().call(h3p.player1().getPlayerId());
        h3p.hand().call(h3p.player2().getPlayerId());
        h3p.hand().check(h3p.player3().getPlayerId());
        assertEquals(FLOP_SIZE, h3p.hand().getCommunityCards().size());
        assertEquals(DECK_SIZE_FLOP, getDeck(h3p.hand()).getSize());

        var ids0Dealer = List.of(
                h3p.player2().getPlayerId(),
                h3p.player3().getPlayerId(),
                h3p.player1().getPlayerId()
        );
        ids0Dealer.forEach(h3p.hand()::check);
        assertEquals(FLOP_SIZE + 1, h3p.hand().getCommunityCards().size());
        assertEquals(DECK_SIZE_FLOP - BURN_AND_DEAL, getDeck(h3p.hand()).getSize());

        ids0Dealer.forEach(h3p.hand()::check);
        assertEquals(FLOP_SIZE + 2, h3p.hand().getCommunityCards().size());
        assertEquals(DECK_SIZE_FLOP - 2 * BURN_AND_DEAL, getDeck(h3p.hand()).getSize());
    }

    private Deck getDeck(Hand hand) {
        try {
            var deckField = Hand.class.getDeclaredField("deck");
            deckField.setAccessible(true);
            return (Deck) deckField.get(hand);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
