package org.proom.engine.hand;

import org.proom.engine.cards.Card;
import org.proom.engine.cards.HandRank;
import org.proom.engine.cards.Ranker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vasyalike
 */
record WinnerFinder(List<Player> players, List<Card> communityCards) {

    List<Player> apply() {
        var winners = new ArrayList<Player>();
        winners.add(players.getFirst());
        var bestRanks = new ArrayList<HandRank>();
        bestRanks.add(new Ranker(communityCards, winners.getFirst().getHoleCards()).getBestHand());
        for (var i = 1; i < players.size(); i++) {
            var playerRank = new Ranker(communityCards, players.get(i).getHoleCards()).getBestHand();
            var rankCompared = bestRanks.getFirst().compareTo(playerRank);
            if (rankCompared < 0) {
                bestRanks.clear();
                winners.clear();
            }
            if (rankCompared <= 0) {
                bestRanks.add(playerRank);
                winners.add(players.get(i));
            }
        }
        for (var i = 0; i < winners.size(); i++) {
            winners.get(i).setHandRank(bestRanks.get(i));
        }
        return winners;
    }
}
