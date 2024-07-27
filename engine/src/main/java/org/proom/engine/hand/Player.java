package org.proom.engine.hand;

import com.github.f4b6a3.uuid.alt.GUID;
import org.proom.engine.cards.Card;
import org.proom.engine.cards.HandRank;
import org.proom.engine.exceptions.NotEnoughChipsToBet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

/**
 * @author vasyalike
 */
public final class Player {

    private static final BigDecimal START_CHIPS = new BigDecimal("10.00");

    private final String externalId;
    private final String playerId;

    private boolean waiting;
    private BigDecimal chips;
    private List<Card> holeCards;
    private boolean fold;
    private boolean check;
    private boolean acted;
    private BigDecimal betAmount;
    private HandRank handRank;

    public Player(String externalId) {
        this(externalId, START_CHIPS);
    }

    public Player(String externalId, BigDecimal chips) {
        this.externalId = externalId;
        this.playerId = GUID.v7().toString();
        this.chips = chips;
        this.betAmount = BigDecimal.ZERO;
        this.waiting = true;
    }

    public void resetNextRound() {
        betAmount = BigDecimal.ZERO;
        setCheck(false);
    }

    public void resetNextHand() {
        resetNextRound();
        fold = false;
        holeCards = emptyList();
        handRank = null;
        waiting = false;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public BigDecimal getChips() {
        return chips;
    }

    public List<Card> getHoleCards() {
        return holeCards;
    }

    public void setHoleCards(List<Card> holeCards) {
        this.holeCards = holeCards;
    }

    public void bet(BigDecimal amount, boolean acted) {
        if (chips.compareTo(amount) < 0) {
            throw new NotEnoughChipsToBet(playerId, chips, amount);
        }
        chips = chips.subtract(amount);
        betAmount = betAmount.add(amount);
        setActed(acted);
    }

    public void bet(BigDecimal amount) {
        bet(amount, true);
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
        setActed(check);
    }

    public boolean isFold() {
        return fold;
    }

    public void setFold() {
        this.fold = true;
        setActed(true);
    }

    public boolean isActed() {
        return acted;
    }

    public void setActed(boolean acted) {
        this.acted = acted;
    }

    public void awardPot(BigDecimal amount) {
        chips = chips.add(amount);
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public BigDecimal getAllInAmount() {
        return betAmount.add(chips);
    }

    public boolean hasChips() {
        return chips.compareTo(BigDecimal.ZERO) != 0;
    }

    public HandRank getHandRank() {
        return handRank;
    }

    public void setHandRank(HandRank handRank) {
        this.handRank = handRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player player)) {
            return false;
        }
        return Objects.equals(externalId, player.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(externalId);
    }
}
