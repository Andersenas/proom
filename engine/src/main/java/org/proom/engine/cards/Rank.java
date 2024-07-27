package org.proom.engine.cards;

import static org.proom.engine.cards.CardConstants.RANK_ACE;
import static org.proom.engine.cards.CardConstants.RANK_EIGHT;
import static org.proom.engine.cards.CardConstants.RANK_FIVE;
import static org.proom.engine.cards.CardConstants.RANK_FOUR;
import static org.proom.engine.cards.CardConstants.RANK_JACK;
import static org.proom.engine.cards.CardConstants.RANK_KING;
import static org.proom.engine.cards.CardConstants.RANK_NINE;
import static org.proom.engine.cards.CardConstants.RANK_QUEEN;
import static org.proom.engine.cards.CardConstants.RANK_SEVEN;
import static org.proom.engine.cards.CardConstants.RANK_SIX;
import static org.proom.engine.cards.CardConstants.RANK_TEN;
import static org.proom.engine.cards.CardConstants.RANK_THREE;
import static org.proom.engine.cards.CardConstants.RANK_TWO;

/**
 * @author vasyalike
 */
public enum Rank {
    TWO("2", RANK_TWO),
    THREE("3", RANK_THREE),
    FOUR("4", RANK_FOUR),
    FIVE("5", RANK_FIVE),
    SIX("6", RANK_SIX),
    SEVEN("7", RANK_SEVEN),
    EIGHT("8", RANK_EIGHT),
    NINE("9", RANK_NINE),
    TEN("10", RANK_TEN),
    JACK("J", RANK_JACK),
    QUEEN("Q", RANK_QUEEN),
    KING("K", RANK_KING),
    ACE("A", RANK_ACE);

    private final String symbol;
    private final int value;

    Rank(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String getSymbol() {
        return symbol;
    }
}
