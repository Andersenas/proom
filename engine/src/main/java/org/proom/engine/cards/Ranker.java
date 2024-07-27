package org.proom.engine.cards;

import java.util.ArrayList;
import java.util.List;

import static org.proom.engine.cards.HandCombinations.COMBINATIONS;

/**
 * @author vasyalike
 */
public final class Ranker {
    private final List<Card> cards;
    private final HandRank bestHand;

    public Ranker(List<Card> communityCards, List<Card> playerCards) {
        cards = new ArrayList<>(communityCards);
        cards.addAll(playerCards);
        bestHand = findBestHand();
    }

    public HandRank getBestHand() {
        return bestHand;
    }

    private HandRank findBestHand() {
        HandRank res = null;
        for (var combinationIndices : COMBINATIONS) {
            var combination = new ArrayList<Card>();
            for (var combinationIndex : combinationIndices) {
                combination.add(cards.get(combinationIndex));
            }
            var handRank = new HandRank(combination);
            if (res == null || handRank.compareTo(res) > 0) {
                res = handRank;
            }
        }
        return res;
    }
}
