package org.proom.engine.cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author vasyalike
 */
class HandCombinationsTest {

    private static final int TOTAL = 21;

    @Test
    public void testAllCombinations() {
        assertEquals(TOTAL, HandCombinations.COMBINATIONS.size());
    }
}
