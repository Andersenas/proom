package org.proom.engine.hand;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.proom.engine.hand.HandState.FINISHED;
import static org.proom.engine.hand.HandState.RIVER;

/**
 * @author vasyalike
 */
public class HandStateTest {

    @Test
    public void testHandStateNext() {
        assertEquals(FINISHED, HandState.next(RIVER));
        assertThrows(IllegalStateException.class, () -> HandState.next(FINISHED));
    }
}
