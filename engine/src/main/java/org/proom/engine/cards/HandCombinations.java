package org.proom.engine.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vasyalike
 */
public final class HandCombinations {
    private HandCombinations() { }

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;

    public static final List<List<Integer>> COMBINATIONS = new ArrayList<>();
    static {
        COMBINATIONS.add(List.of(ZERO, ONE, TWO, THREE, FOUR));
        COMBINATIONS.add(List.of(ZERO, ONE, TWO, THREE, FIVE));
        COMBINATIONS.add(List.of(ZERO, ONE, TWO, THREE, SIX));
        COMBINATIONS.add(List.of(ZERO, ONE, TWO, FOUR, FIVE));
        COMBINATIONS.add(List.of(ZERO, ONE, TWO, FOUR, SIX));
        COMBINATIONS.add(List.of(ZERO, ONE, TWO, FIVE, SIX));
        COMBINATIONS.add(List.of(ZERO, ONE, THREE, FOUR, FIVE));
        COMBINATIONS.add(List.of(ZERO, ONE, THREE, FOUR, SIX));
        COMBINATIONS.add(List.of(ZERO, ONE, THREE, FIVE, SIX));
        COMBINATIONS.add(List.of(ZERO, ONE, FOUR, FIVE, SIX));
        COMBINATIONS.add(List.of(ZERO, TWO, THREE, FOUR, FIVE));
        COMBINATIONS.add(List.of(ZERO, TWO, THREE, FOUR, SIX));
        COMBINATIONS.add(List.of(ZERO, TWO, THREE, FIVE, SIX));
        COMBINATIONS.add(List.of(ZERO, TWO, FOUR, FIVE, SIX));
        COMBINATIONS.add(List.of(ZERO, THREE, FOUR, FIVE, SIX));
        COMBINATIONS.add(List.of(ONE, TWO, THREE, FOUR, FIVE));
        COMBINATIONS.add(List.of(ONE, TWO, THREE, FOUR, SIX));
        COMBINATIONS.add(List.of(ONE, TWO, THREE, FIVE, SIX));
        COMBINATIONS.add(List.of(ONE, TWO, FOUR, FIVE, SIX));
        COMBINATIONS.add(List.of(ONE, THREE, FOUR, FIVE, SIX));
        COMBINATIONS.add(List.of(TWO, THREE, FOUR, FIVE, SIX));
    }
}
