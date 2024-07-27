package org.proom.engine.cards;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.naturalOrder;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * @author vasyalike
 */
public final class HandRank implements Comparable<HandRank> {
    private final List<Card> hand;
    private final Map<Rank, Long> rankCounts;
    private final HandType type;

    private static final int FOUR_OF_A_KIND_COUNT = 4;
    private static final int THREE_OF_A_KIND_COUNT = 3;
    private static final int PAIR_COUNT = 2;

    public HandRank(List<Card> hand) {
        this.hand = new ArrayList<>(hand);
        this.hand.sort(Comparator.comparingInt(card -> card.getRank().getValue()));
        this.rankCounts = this.hand.stream().collect(groupingBy(Card::getRank, counting()));
        this.type = findType();
    }

    public List<Card> getHand() {
        return unmodifiableList(hand);
    }

    public HandType getType() {
        return type;
    }

    @Override
    public int compareTo(HandRank other) {
        if (getType().ordinal() < other.getType().ordinal()) {
            return 1;
        }
        if (getType().ordinal() > other.getType().ordinal()) {
            return -1;
        }
        return switch (getType()) {
            case STRAIGHT_FLUSH, STRAIGHT -> getStraightAceHighCard().compareTo(other.getStraightAceHighCard());
            case FOUR_OF_A_KIND -> {
                var res = requireNonNull(rankCountedFirst(FOUR_OF_A_KIND_COUNT))
                        .compareTo(requireNonNull(other.rankCountedFirst(FOUR_OF_A_KIND_COUNT)));
                yield res != 0
                        ? res
                        : requireNonNull(rankCountedFirst(1)).compareTo(requireNonNull(other.rankCountedFirst(1)));
            }
            case FULL_HOUSE -> {
                var res = requireNonNull(rankCountedFirst(THREE_OF_A_KIND_COUNT))
                        .compareTo(requireNonNull(other.rankCountedFirst(THREE_OF_A_KIND_COUNT)));
                yield res != 0 ? res : requireNonNull(rankCountedFirst(PAIR_COUNT))
                        .compareTo(requireNonNull(other.rankCountedFirst(PAIR_COUNT)));
            }
            case THREE_OF_A_KIND -> {
                var res = requireNonNull(rankCountedFirst(THREE_OF_A_KIND_COUNT))
                        .compareTo(requireNonNull(other.rankCountedFirst(THREE_OF_A_KIND_COUNT)));
                if (res != 0) {
                    yield res;
                }
                yield compareRanks(
                        getNotRankCounted(THREE_OF_A_KIND_COUNT),
                        other.getNotRankCounted(THREE_OF_A_KIND_COUNT)
                );
            }
            case TWO_PAIRS, PAIR -> {
                var res = compareRanks(rankCounted(PAIR_COUNT), other.rankCounted(PAIR_COUNT));
                yield res != 0
                        ? res
                        : compareRanks(getNotRankCounted(PAIR_COUNT), other.getNotRankCounted(PAIR_COUNT));
            }
            default -> compareRanks(other);
        };
    }

    private Rank getStraightAceHighCard() {
        return isAceHigh() ? Rank.FIVE : hand.getLast().getRank();
    }

    private List<Rank> getNotRankCounted(int rankCount) {
        return hand.stream().map(Card::getRank).filter(rank -> !rankCounted(rankCount).contains(rank)).toList();
    }

    private int compareRanks(List<Rank> ranks, List<Rank> otherRanks) {
        for (var i = ranks.size() - 1; i >= 0; i--) {
            var res = ranks.get(i).compareTo(otherRanks.get(i));
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    private int compareRanks(HandRank other) {
        return compareRanks(
                hand.stream().map(Card::getRank).toList(),
                other.hand.stream().map(Card::getRank).toList()
        );
    }

    private HandType findType() {
        if (isFlush()) {
            return isStraight() ? HandType.STRAIGHT_FLUSH : HandType.FLUSH;
        }
        if (isStraight()) {
            return HandType.STRAIGHT;
        }

        if (rankCountedFirst(FOUR_OF_A_KIND_COUNT) != null) {
            return HandType.FOUR_OF_A_KIND;
        }
        if (rankCountedFirst(PAIR_COUNT) != null && rankCountedFirst(THREE_OF_A_KIND_COUNT) != null) {
            return HandType.FULL_HOUSE;
        }
        if (rankCountedFirst(THREE_OF_A_KIND_COUNT) != null) {
            return HandType.THREE_OF_A_KIND;
        }
        if (rankCounted(PAIR_COUNT).size() == 2) {
            return HandType.TWO_PAIRS;
        }
        if (rankCountedFirst(PAIR_COUNT) != null) {
            return HandType.PAIR;
        }
        return HandType.HIGH_CARD;
    }

    private boolean isFlush() {
        var suits = hand.stream().map(Card::getSuit).collect(toSet());
        return suits.size() == 1;
    }

    private boolean isStraight() {
        for (var i = 1; i < hand.size(); i++) {
            if (hand.get(i).getRank().getValue() != hand.get(i - 1).getRank().getValue() + 1) {
                if (i != hand.size() - 1) {
                    return false;
                } else {
                    return isAceHigh();
                }
            }
        }
        return true;
    }

    private boolean isAceHigh() {
        return hand.getLast().getRank() == Rank.ACE && hand.getFirst().getRank() == Rank.TWO;
    }

    private List<Rank> rankCounted(int count) {
        var res = new ArrayList<Rank>();
        for (var rankCount : rankCounts.entrySet()) {
            if (rankCount.getValue().intValue() == count) {
                res.add(rankCount.getKey());
            }
        }
        res.sort(naturalOrder());
        return res;
    }

    private Rank rankCountedFirst(int count) {
        var ranks = rankCounted(count);
        return ranks.isEmpty() ? null : ranks.getFirst();
    }
}
