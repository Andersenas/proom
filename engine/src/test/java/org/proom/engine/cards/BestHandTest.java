package org.proom.engine.cards;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.proom.engine.cards.Card.ACE_OF_HEARTS;
import static org.proom.engine.cards.Card.EIGHT_OF_CLUBS;
import static org.proom.engine.cards.Card.FIVE_OF_SPADES;
import static org.proom.engine.cards.Card.FOUR_OF_CLUBS;
import static org.proom.engine.cards.Card.FOUR_OF_DIAMONDS;
import static org.proom.engine.cards.Card.FOUR_OF_HEARTS;
import static org.proom.engine.cards.Card.FOUR_OF_SPADES;
import static org.proom.engine.cards.Card.JACK_OF_HEARTS;
import static org.proom.engine.cards.Card.JACK_OF_SPADES;
import static org.proom.engine.cards.Card.KING_OF_CLUBS;
import static org.proom.engine.cards.Card.KING_OF_HEARTS;
import static org.proom.engine.cards.Card.NINE_OF_DIAMONDS;
import static org.proom.engine.cards.Card.QUEEN_OF_DIAMONDS;
import static org.proom.engine.cards.Card.QUEEN_OF_HEARTS;
import static org.proom.engine.cards.Card.SEVEN_OF_CLUBS;
import static org.proom.engine.cards.Card.SEVEN_OF_HEARTS;
import static org.proom.engine.cards.Card.SIX_OF_CLUBS;
import static org.proom.engine.cards.Card.SIX_OF_DIAMONDS;
import static org.proom.engine.cards.Card.SIX_OF_HEARTS;
import static org.proom.engine.cards.Card.TEN_OF_CLUBS;
import static org.proom.engine.cards.Card.TEN_OF_HEARTS;
import static org.proom.engine.cards.Card.THREE_OF_CLUBS;
import static org.proom.engine.cards.Card.TWO_OF_HEARTS;
import static org.proom.engine.cards.Card.TWO_OF_SPADES;
import static org.proom.engine.cards.HandType.FLUSH;
import static org.proom.engine.cards.HandType.FOUR_OF_A_KIND;
import static org.proom.engine.cards.HandType.FULL_HOUSE;
import static org.proom.engine.cards.HandType.PAIR;
import static org.proom.engine.cards.HandType.STRAIGHT;
import static org.proom.engine.cards.HandType.STRAIGHT_FLUSH;
import static org.proom.engine.cards.HandType.THREE_OF_A_KIND;
import static org.proom.engine.cards.HandType.TWO_PAIRS;

/**
 * @author vasyalike
 */
public class BestHandTest {

    @Test
    public void testRoyalFlush() {
        var bestRank = new Ranker(
                List.of(ACE_OF_HEARTS, KING_OF_HEARTS, QUEEN_OF_HEARTS, JACK_OF_HEARTS, TWO_OF_SPADES),
                List.of(TEN_OF_HEARTS, TWO_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(TEN_OF_HEARTS, JACK_OF_HEARTS, QUEEN_OF_HEARTS, KING_OF_HEARTS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(STRAIGHT_FLUSH, bestRank.getType());
    }

    @Test
    public void testFlush() {
        var bestRank = new Ranker(
                List.of(ACE_OF_HEARTS, KING_OF_HEARTS, QUEEN_OF_HEARTS, JACK_OF_HEARTS, TWO_OF_SPADES),
                List.of(THREE_OF_CLUBS, TWO_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(TWO_OF_HEARTS, JACK_OF_HEARTS, QUEEN_OF_HEARTS, KING_OF_HEARTS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(FLUSH, bestRank.getType());
    }

    @Test
    public void testStraight() {
        var bestRank = new Ranker(
                List.of(ACE_OF_HEARTS, KING_OF_CLUBS, QUEEN_OF_DIAMONDS, JACK_OF_SPADES, TWO_OF_SPADES),
                List.of(TEN_OF_CLUBS, TWO_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(TEN_OF_CLUBS, JACK_OF_SPADES, QUEEN_OF_DIAMONDS, KING_OF_CLUBS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(STRAIGHT, bestRank.getType());
    }

    @Test
    public void testAceLowStraight() {
        var bestRank = new Ranker(
                List.of(ACE_OF_HEARTS, KING_OF_CLUBS, THREE_OF_CLUBS, FOUR_OF_HEARTS, FIVE_OF_SPADES),
                List.of(TEN_OF_CLUBS, TWO_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(TWO_OF_HEARTS, THREE_OF_CLUBS, FOUR_OF_HEARTS, FIVE_OF_SPADES, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(STRAIGHT, bestRank.getType());
    }

    @Test
    public void testInvalidAceLowStraight() {
        var bestRank = new Ranker(
                List.of(FOUR_OF_HEARTS, FIVE_OF_SPADES, SIX_OF_DIAMONDS, ACE_OF_HEARTS, SEVEN_OF_HEARTS),
                List.of(EIGHT_OF_CLUBS, THREE_OF_CLUBS)
        ).getBestHand();
        assertEquals(
                List.of(FOUR_OF_HEARTS, FIVE_OF_SPADES, SIX_OF_DIAMONDS, SEVEN_OF_HEARTS, EIGHT_OF_CLUBS),
                bestRank.getHand()
        );
        assertEquals(STRAIGHT, bestRank.getType());
    }

    @Test
    public void test4OfAKind() {
        var bestRank = new Ranker(
                List.of(FOUR_OF_HEARTS, FOUR_OF_SPADES, FOUR_OF_CLUBS, ACE_OF_HEARTS, SEVEN_OF_HEARTS),
                List.of(FOUR_OF_DIAMONDS, THREE_OF_CLUBS)
        ).getBestHand();
        assertEquals(
                List.of(FOUR_OF_HEARTS, FOUR_OF_SPADES, FOUR_OF_CLUBS, FOUR_OF_DIAMONDS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(FOUR_OF_A_KIND, bestRank.getType());
    }

    @Test
    public void test3OfAKind() {
        var bestRank = new Ranker(
                List.of(FOUR_OF_HEARTS, FOUR_OF_SPADES, FIVE_OF_SPADES, ACE_OF_HEARTS, SEVEN_OF_HEARTS),
                List.of(FOUR_OF_DIAMONDS, THREE_OF_CLUBS)
        ).getBestHand();
        assertEquals(
                List.of(FOUR_OF_HEARTS, FOUR_OF_SPADES, FOUR_OF_DIAMONDS, SEVEN_OF_HEARTS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(THREE_OF_A_KIND, bestRank.getType());
    }

    @Test
    public void testPair() {
        var bestRank = new Ranker(
                List.of(FOUR_OF_HEARTS, SIX_OF_DIAMONDS, FIVE_OF_SPADES, ACE_OF_HEARTS, SEVEN_OF_HEARTS),
                List.of(FOUR_OF_DIAMONDS, TWO_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(FOUR_OF_HEARTS, FOUR_OF_DIAMONDS, SIX_OF_DIAMONDS, SEVEN_OF_HEARTS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(PAIR, bestRank.getType());
    }

    @Test
    public void testFullHouse() {
        var bestRank = new Ranker(
                List.of(FOUR_OF_HEARTS, SIX_OF_DIAMONDS, SIX_OF_CLUBS, ACE_OF_HEARTS, SEVEN_OF_CLUBS),
                List.of(FOUR_OF_DIAMONDS, SIX_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(FOUR_OF_HEARTS, FOUR_OF_DIAMONDS, SIX_OF_DIAMONDS, SIX_OF_CLUBS, SIX_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(FULL_HOUSE, bestRank.getType());
    }

    @Test
    public void testTwoPairs() {
        var bestRank = new Ranker(
                List.of(FOUR_OF_HEARTS, SIX_OF_DIAMONDS, NINE_OF_DIAMONDS, ACE_OF_HEARTS, SEVEN_OF_CLUBS),
                List.of(FOUR_OF_DIAMONDS, SIX_OF_HEARTS)
        ).getBestHand();
        assertEquals(
                List.of(FOUR_OF_HEARTS, FOUR_OF_DIAMONDS, SIX_OF_DIAMONDS, SIX_OF_HEARTS, ACE_OF_HEARTS),
                bestRank.getHand()
        );
        assertEquals(TWO_PAIRS, bestRank.getType());
    }
}
