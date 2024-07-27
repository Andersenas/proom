package org.proom.engine.cards;

import org.junit.jupiter.api.Test;
import org.proom.engine.exceptions.EmptyDeckException;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.proom.engine.cards.Card.ACE_OF_CLUBS;
import static org.proom.engine.cards.Card.SIX_OF_SPADES;
import static org.proom.engine.cards.Card.TWO_OF_CLUBS;

/**
 * @author vasyalike
 */
public class DeckTest {

    @Test
    public void testShuffle() {
        var deck = new Deck(List.of(ACE_OF_CLUBS, SIX_OF_SPADES, TWO_OF_CLUBS));
        var size = deck.getSize();
        assertEquals(ACE_OF_CLUBS, deck.nextCard());
        assertEquals(size - 1, deck.getSize());
        assertEquals(SIX_OF_SPADES, deck.nextCard());
        assertEquals(size - 2, deck.getSize());
    }

    @Test
    public void testEmptyDeck() {
        var deck = new Deck();
        assertEquals(Card.values().length, deck.getSize());
        IntStream.range(0, Card.values().length).forEach(_ -> deck.nextCard());
        assertThrows(EmptyDeckException.class, deck::nextCard);
    }
}
