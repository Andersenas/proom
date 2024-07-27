package org.proom.engine.game;

import org.junit.jupiter.api.Test;
import org.proom.engine.cards.HandType;
import org.proom.engine.hand.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author vasyalike
 */
public final class SimulationTest {

    private static final String ID = "eid";
    private static final int TOTAL_PLAYERS = 6;
    private static final int SIMULATION_HANDS = 50;
    private static final boolean OUTPUT = false;

    @Test
    public void testSimulation() {
        for (int i = 0; i < SIMULATION_HANDS; i++) {
            outputLn("===" + (i + 1) + "===");
            singleHand();
        }
        outputLn(FREQUENCIES);
    }

    private static final Map<HandType, Integer> FREQUENCIES = new LinkedHashMap<>();
    static {
        for (var type : HandType.values()) {
            FREQUENCIES.put(type, 0);
        }
    }

    private void outputLn(Object o) {
        if (OUTPUT && o != null) {
            System.out.println(o);
        }
    }

    private void output(Object o) {
        if (OUTPUT && o != null) {
            System.out.print(o);
        }
    }

    private void singleHand() {
        var board = new Board();
        var player1 = new Player(ID + "_0");
        var player2 = new Player(ID + "_1");
        var player3 = new Player(ID + "_2");
        var player4 = new Player(ID + "_3");
        var player5 = new Player(ID + "_4");
        var player6 = new Player(ID + "_5");
        var players = List.of(player1, player2, player3, player4, player5, player6);
        for (var i = 0; i < TOTAL_PLAYERS; i++) {
            board.sitIn(players.get(i), i);
        }

        var hand = board.newHand();
        hand.call(player4.getPlayerId());
        hand.call(player5.getPlayerId());
        hand.call(player6.getPlayerId());
        hand.call(player1.getPlayerId());
        hand.call(player2.getPlayerId());
        hand.check(player3.getPlayerId());

        Function<Player, String> prefix = p -> p.getExternalId().split("_")[1] + ". ";

        players.forEach(p -> output(prefix.apply(p) + p.getHoleCards() + ";\t"));
        List.of(player2, player3, player4, player5, player6, player1).forEach(p -> hand.check(p.getPlayerId()));
        List.of(player2, player3, player4, player5, player6, player1).forEach(p -> hand.check(p.getPlayerId()));
        List.of(player2, player3, player4, player5, player6, player1).forEach(p -> hand.check(p.getPlayerId()));
        outputLn("");
        output(hand.getCommunityCards() + "; ");
        players.stream().filter(p -> p.getHandRank() != null).forEach(p -> {
            FREQUENCIES.compute(p.getHandRank().getType(), (_, v) -> (v == null ? 0 : v) + 1);
            output(prefix.apply(p) + p.getHandRank().getType() + "\t" + p.getHandRank().getHand() + "\t");
        });
        outputLn("");
    }
}
