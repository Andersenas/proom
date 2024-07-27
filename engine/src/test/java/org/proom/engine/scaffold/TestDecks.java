package org.proom.engine.scaffold;

import org.proom.engine.cards.Card;
import org.proom.engine.cards.Deck;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author vasyalike
 */
public final class TestDecks {
    private TestDecks() { }

    private static final Card[] HEADS_UP_DEALER_LOOSE = new Card[] {
            Card.KING_OF_CLUBS, Card.FIVE_OF_DIAMONDS,
            Card.QUEEN_OF_DIAMONDS, Card.EIGHT_OF_HEARTS,
            Card.TWO_OF_SPADES,
            Card.ACE_OF_CLUBS, Card.JACK_OF_HEARTS, Card.NINE_OF_DIAMONDS,
            Card.TWO_OF_HEARTS, Card.NINE_OF_CLUBS,
            Card.TWO_OF_DIAMONDS, Card.QUEEN_OF_HEARTS
    };

    private static final Card[] THREE_PLAYERS_DEALER_WIN = new Card[] {
            Card.KING_OF_CLUBS, Card.FIVE_OF_DIAMONDS, Card.QUEEN_OF_DIAMONDS,
            Card.EIGHT_OF_HEARTS, Card.THREE_OF_DIAMONDS, Card.JACK_OF_SPADES,
            Card.TWO_OF_SPADES,
            Card.EIGHT_OF_DIAMONDS, Card.JACK_OF_HEARTS, Card.NINE_OF_DIAMONDS,
            Card.TWO_OF_HEARTS, Card.NINE_OF_CLUBS,
            Card.TWO_OF_DIAMONDS, Card.JACK_OF_CLUBS
    };

    private static final Card[] HEADS_UP_SPLIT_POT = new Card[] {
            Card.JACK_OF_SPADES, Card.TEN_OF_SPADES, Card.TEN_OF_CLUBS, Card.THREE_OF_DIAMONDS,
            Card.TWO_OF_SPADES,
            Card.FIVE_OF_HEARTS, Card.QUEEN_OF_HEARTS, Card.TEN_OF_DIAMONDS,
            Card.TWO_OF_HEARTS, Card.EIGHT_OF_SPADES,
            Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_DIAMONDS
    };

    public static Deck headsUpDealerLooseDeck() throws Exception {
        return createTestDeck(HEADS_UP_DEALER_LOOSE);
    }

    public static Deck headsUpSplitPotDeck() throws Exception {
        return createTestDeck(HEADS_UP_SPLIT_POT);
    }

    public static Deck threePlayersDealerWinDeck() throws Exception {
        return createTestDeck(THREE_PLAYERS_DEALER_WIN);
    }

    private static Deck createTestDeck(Card... cards) throws Exception {
        var c = Deck.class.getDeclaredConstructor(List.class);
        c.setAccessible(true);
        return c.newInstance(asList(cards));
    }
}
