package org.proom.engine.scaffold;

import org.proom.engine.Hand2Players;
import org.proom.engine.Hand3Players;
import org.proom.engine.cards.Deck;
import org.proom.engine.hand.Hand;
import org.proom.engine.hand.Player;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author vasyalike
 */
public final class TestHands {
    private TestHands() { }

    public static final BigDecimal SMALL_BLIND = new BigDecimal("0.05");
    public static final BigDecimal BIG_BLIND = SMALL_BLIND.multiply(BigDecimal.TWO);

    public static Hand2Players hand2Players(int buttonIndex) throws Exception {
        return hand2Players(buttonIndex, new Deck());
    }

    public static Hand2Players hand2Players(int buttonIndex, Deck deck) throws Exception {
        var player1 = new Player("id0");
        var player2 = new Player("id1");
        return new Hand2Players(
                createTestHand(List.of(player1, player2), buttonIndex, deck),
                player1,
                player2
        );
    }

    public static Hand3Players hand3Players(int buttonIndex) throws Exception {
        return hand3Players(buttonIndex, new Deck());
    }

    public static Hand3Players hand3Players(int buttonIndex, Deck deck) throws Exception {
        var player1 = new Player("id0");
        var player2 = new Player("id1");
        var player3 = new Player("id3");
        return new Hand3Players(
                createTestHand(List.of(player1, player2, player3), buttonIndex, deck),
                player1,
                player2,
                player3
        );
    }

    private static Hand createTestHand(List<Player> players, int buttonIndex, Deck deck) throws Exception {
        var c = Hand.class.getDeclaredConstructor(List.class, Integer.TYPE, BigDecimal.class, Deck.class);
        c.setAccessible(true);
        return c.newInstance(players, buttonIndex, SMALL_BLIND, deck);
    }
}
