package org.proom.engine.cards;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.proom.engine.cards.Card.ACE_OF_CLUBS;
import static org.proom.engine.cards.Card.ACE_OF_DIAMONDS;
import static org.proom.engine.cards.Card.ACE_OF_HEARTS;
import static org.proom.engine.cards.Card.ACE_OF_SPADES;
import static org.proom.engine.cards.Card.EIGHT_OF_CLUBS;
import static org.proom.engine.cards.Card.FIVE_OF_CLUBS;
import static org.proom.engine.cards.Card.FIVE_OF_SPADES;
import static org.proom.engine.cards.Card.FOUR_OF_CLUBS;
import static org.proom.engine.cards.Card.FOUR_OF_DIAMONDS;
import static org.proom.engine.cards.Card.FOUR_OF_HEARTS;
import static org.proom.engine.cards.Card.FOUR_OF_SPADES;
import static org.proom.engine.cards.Card.JACK_OF_SPADES;
import static org.proom.engine.cards.Card.KING_OF_CLUBS;
import static org.proom.engine.cards.Card.KING_OF_DIAMONDS;
import static org.proom.engine.cards.Card.KING_OF_SPADES;
import static org.proom.engine.cards.Card.QUEEN_OF_SPADES;
import static org.proom.engine.cards.Card.SEVEN_OF_CLUBS;
import static org.proom.engine.cards.Card.SEVEN_OF_HEARTS;
import static org.proom.engine.cards.Card.SIX_OF_CLUBS;
import static org.proom.engine.cards.Card.SIX_OF_DIAMONDS;
import static org.proom.engine.cards.Card.SIX_OF_SPADES;
import static org.proom.engine.cards.Card.TEN_OF_SPADES;
import static org.proom.engine.cards.Card.THREE_OF_CLUBS;
import static org.proom.engine.cards.Card.THREE_OF_DIAMONDS;
import static org.proom.engine.cards.Card.THREE_OF_HEARTS;
import static org.proom.engine.cards.Card.THREE_OF_SPADES;
import static org.proom.engine.cards.Card.TWO_OF_CLUBS;
import static org.proom.engine.cards.Card.TWO_OF_DIAMONDS;
import static org.proom.engine.cards.Card.TWO_OF_HEARTS;
import static org.proom.engine.cards.Card.TWO_OF_SPADES;

/**
 * @author vasyalike
 */
class HandRankTest {

    @Test
    void testCompareTo() {
        var cards = of(ACE_OF_DIAMONDS, ACE_OF_HEARTS, ACE_OF_SPADES, ACE_OF_CLUBS, TWO_OF_SPADES);
        assertEquals(0, new HandRank(cards).compareTo(new HandRank(cards)));
    }

    @Test
    void testStraightFlushAceHigh() {
        testNegativeComparison(
                of(ACE_OF_SPADES, TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, FIVE_OF_SPADES),
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, FIVE_OF_SPADES, SIX_OF_SPADES)
        );
    }

    @Test
    void testFourOfAKindQuadruplet() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_HEARTS, TWO_OF_CLUBS, TWO_OF_DIAMONDS, ACE_OF_SPADES),
                of(THREE_OF_SPADES, THREE_OF_HEARTS, THREE_OF_CLUBS, THREE_OF_DIAMONDS, KING_OF_SPADES)
        );
    }

    @Test
    void testFourOfAKindKicker() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_HEARTS, TWO_OF_CLUBS, TWO_OF_DIAMONDS, KING_OF_SPADES),
                of(TWO_OF_SPADES, TWO_OF_HEARTS, TWO_OF_CLUBS, TWO_OF_DIAMONDS, ACE_OF_SPADES)
        );
    }

    @Test
    void testFullHouseTriplet() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_HEARTS, THREE_OF_CLUBS, THREE_OF_DIAMONDS, THREE_OF_HEARTS),
                of(TWO_OF_SPADES, TWO_OF_HEARTS, FOUR_OF_CLUBS, FOUR_OF_DIAMONDS, FOUR_OF_HEARTS)
        );
    }

    @Test
    void testFullHousePair() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_HEARTS, THREE_OF_CLUBS, THREE_OF_DIAMONDS, THREE_OF_HEARTS),
                of(FOUR_OF_SPADES, FOUR_OF_HEARTS, THREE_OF_CLUBS, THREE_OF_DIAMONDS, THREE_OF_HEARTS)
        );
    }

    @Test
    void testFlushHighest() {
        testNegativeComparison(
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES),
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, SIX_OF_SPADES, ACE_OF_SPADES)
        );
    }

    @Test
    void testFlushNextToHighest() {
        testNegativeComparison(
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, JACK_OF_SPADES, KING_OF_SPADES),
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES)
        );
    }

    @Test
    void testFlushMiddle() {
        testNegativeComparison(
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES),
                of(TWO_OF_SPADES, THREE_OF_SPADES, FIVE_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES)
        );
    }

    @Test
    void testFlushSecond() {
        testNegativeComparison(
                of(TWO_OF_SPADES, THREE_OF_SPADES, JACK_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES),
                of(TWO_OF_SPADES, FOUR_OF_SPADES, JACK_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES)
        );
    }

    @Test
    void testFlushFirst() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TEN_OF_SPADES, JACK_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES),
                of(THREE_OF_SPADES, TEN_OF_SPADES, JACK_OF_SPADES, QUEEN_OF_SPADES, KING_OF_SPADES)
        );
    }

    @Test
    void testStraight() {
        testNegativeComparison(
                of(TWO_OF_SPADES, THREE_OF_SPADES, FOUR_OF_SPADES, FIVE_OF_SPADES, SIX_OF_DIAMONDS),
                of(THREE_OF_SPADES, FOUR_OF_SPADES, FIVE_OF_CLUBS, SIX_OF_CLUBS, SEVEN_OF_CLUBS)
        );
    }

    @Test
    void testThreeOfAKind() {
        testNegativeComparison(
                of(KING_OF_SPADES, KING_OF_CLUBS, KING_OF_DIAMONDS, FIVE_OF_SPADES, SIX_OF_DIAMONDS),
                of(ACE_OF_SPADES, ACE_OF_CLUBS, ACE_OF_DIAMONDS, FOUR_OF_SPADES, FIVE_OF_CLUBS)
        );
    }

    @Test
    void testThreeOfAKindKicker() {
        testNegativeComparison(
                of(KING_OF_SPADES, KING_OF_CLUBS, KING_OF_DIAMONDS, FIVE_OF_SPADES, SIX_OF_DIAMONDS),
                of(KING_OF_SPADES, KING_OF_CLUBS, KING_OF_DIAMONDS, FIVE_OF_SPADES, SEVEN_OF_CLUBS)
        );
    }

    @Test
    void testThreeOfAKindLowKicker() {
        testNegativeComparison(
                of(KING_OF_SPADES, KING_OF_CLUBS, KING_OF_DIAMONDS, FIVE_OF_SPADES, SEVEN_OF_CLUBS),
                of(KING_OF_SPADES, KING_OF_CLUBS, KING_OF_DIAMONDS, SIX_OF_SPADES, SEVEN_OF_CLUBS)
        );
    }

    @Test
    void testTwoPairsHigh() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SIX_OF_SPADES, ACE_OF_SPADES),
                of(THREE_OF_SPADES, THREE_OF_CLUBS, SEVEN_OF_HEARTS, SEVEN_OF_CLUBS, ACE_OF_SPADES)
        );
    }

    @Test
    void testTwoPairsLow() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SIX_OF_SPADES, ACE_OF_SPADES),
                of(THREE_OF_SPADES, THREE_OF_CLUBS, SIX_OF_CLUBS, SIX_OF_SPADES, ACE_OF_SPADES)
        );
    }

    @Test
    void testTwoPairsKicker() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SIX_OF_SPADES, KING_OF_CLUBS),
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SIX_OF_SPADES, ACE_OF_SPADES)
        );
    }

    @Test
    void testPair() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SEVEN_OF_CLUBS, KING_OF_CLUBS),
                of(THREE_OF_SPADES, THREE_OF_CLUBS, SIX_OF_CLUBS, SEVEN_OF_CLUBS, KING_OF_CLUBS)
        );
    }

    @Test
    void testPairKicker() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SEVEN_OF_CLUBS, KING_OF_CLUBS),
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SEVEN_OF_CLUBS, ACE_OF_CLUBS)
        );
    }

    @Test
    void testPairMidKicker() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, SEVEN_OF_CLUBS, KING_OF_CLUBS),
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, EIGHT_OF_CLUBS, KING_OF_CLUBS)
        );
    }

    @Test
    void testPairLowKicker() {
        testNegativeComparison(
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SIX_OF_CLUBS, EIGHT_OF_CLUBS, KING_OF_CLUBS),
                of(TWO_OF_SPADES, TWO_OF_CLUBS, SEVEN_OF_CLUBS, EIGHT_OF_CLUBS, KING_OF_CLUBS)
        );
    }

    @Test
    void testKicker() {
        testNegativeComparison(
                of(TWO_OF_SPADES, THREE_OF_CLUBS, SIX_OF_CLUBS, EIGHT_OF_CLUBS, KING_OF_CLUBS),
                of(TWO_OF_SPADES, THREE_OF_CLUBS, SIX_OF_CLUBS, EIGHT_OF_CLUBS, ACE_OF_CLUBS)
        );
    }

    private void testNegativeComparison(List<Card> cardsLow, List<Card> cardsHigh) {
        var lowHand = new HandRank(cardsLow);
        var highHand = new HandRank(cardsHigh);
        assertTrue(0 > lowHand.compareTo(highHand));
    }
}
