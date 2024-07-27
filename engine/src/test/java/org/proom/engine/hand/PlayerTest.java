package org.proom.engine.hand;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author vasyalike
 */
class PlayerTest {

    @Test
    public void testHashCode() {
        assertEquals("eid".hashCode(), new Player("eid").hashCode());
    }

    @Test
    public void testEquals() {
        var player = new Player("eid");
        assertNotEquals(player, new Object());

        Player player1;
        player1 = player;
        assertEquals(player, player1);
    }
}
