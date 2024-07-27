package org.proom.engine.cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.proom.engine.cards.CardConstants.RANK_KING;
import static org.proom.engine.cards.Rank.ACE;
import static org.proom.engine.cards.Suit.HEARTS;

public class CardTest {

    private static final int DECK_SIZE = 52;

    @Test
    public void testCard() {
        assertEquals(DECK_SIZE, Card.values().length);
        assertEquals(ACE, Card.ACE_OF_HEARTS.getRank());
        assertEquals(RANK_KING, Card.KING_OF_SPADES.getRank().getValue());
        assertEquals(HEARTS, Card.ACE_OF_HEARTS.getSuit());
        assertEquals("A♦", Card.ACE_OF_DIAMONDS.toString());
    }
}
