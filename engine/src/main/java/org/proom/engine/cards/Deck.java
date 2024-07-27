package org.proom.engine.cards;

import org.proom.engine.exceptions.EmptyDeckException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;
import static java.util.Collections.shuffle;

/**
 * @author vasyalike
 */
public final class Deck {
    private final List<Card> cards;
    private final AtomicInteger currentCardIndex = new AtomicInteger();

    static final int FLOP_SIZE = 3;

    Deck(List<Card> cards) {
        this.cards = cards;
    }

    public Deck() {
        cards = new ArrayList<>(asList(Card.values()));
        shuffle(cards, new SecureRandom());
    }

    public int getSize() {
        return cards.size() - currentCardIndex.get();
    }

    public Card nextCard() {
        try {
            return cards.get(currentCardIndex.getAndIncrement());
        } catch (IndexOutOfBoundsException e) {
            throw new EmptyDeckException();
        }
    }

    public List<Card> burnAndDealFlop() {
        return burnAndDeal(FLOP_SIZE);
    }

    public List<Card> burnAndDealSingle() {
        return burnAndDeal(1);
    }

    private List<Card> burnAndDeal(int cardsCount) {
        burnCard();
        var res = new ArrayList<Card>(cardsCount);
        for (var i = 0; i < cardsCount; i++) {
            res.add(nextCard());
        }
        return res;
    }

    public List<List<Card>> dealHoleCards(int playersCount) {
        var res = new ArrayList<List<Card>>(playersCount);
        for (var i = 0; i < playersCount; i++) {
            var cards = new ArrayList<Card>();
            cards.add(nextCard());
            res.add(cards);
        }
        for (var i = 0; i < playersCount; i++) {
            res.get(i).add(nextCard());
        }
        return res;
    }

    private void burnCard() {
        nextCard();
    }
}
