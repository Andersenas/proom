package org.proom.engine.cards;

import static org.proom.engine.cards.CardConstants.CLUBS_SYMBOL;
import static org.proom.engine.cards.CardConstants.DIAMONDS_SYMBOL;
import static org.proom.engine.cards.CardConstants.HEARTS_SYMBOL;
import static org.proom.engine.cards.CardConstants.SPADES_SYMBOL;

/**
 * @author vasyalike
 */
public enum Suit {
    HEARTS(HEARTS_SYMBOL),
    DIAMONDS(DIAMONDS_SYMBOL),
    CLUBS(CLUBS_SYMBOL),
    SPADES(SPADES_SYMBOL);

    private final char symbol;

    Suit(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
