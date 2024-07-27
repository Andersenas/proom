package org.proom.engine.game;

import com.github.f4b6a3.uuid.alt.GUID;
import org.proom.engine.exceptions.ChosenSeatException;
import org.proom.engine.exceptions.DuplicatePlayerException;
import org.proom.engine.exceptions.InvalidPositionException;
import org.proom.engine.exceptions.MaxPlayersException;
import org.proom.engine.exceptions.InvalidStartHandException;
import org.proom.engine.hand.Hand;
import org.proom.engine.hand.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author vasyalike
 */
public final class Board {

    public static final int MAX_PLAYERS = 6;
    static final BigDecimal SMALL_BLIND = new BigDecimal("0.05");

    private final String id;
    private final int maxPlayers;
    private final Map<Integer, Player> players;
    private final BigDecimal smallBlind;

    private int buttonIndex;
    private Hand hand;

    public Board() {
        this(MAX_PLAYERS);
    }

    public Board(int maxPlayers) {
        id = GUID.v7().toString();
        this.maxPlayers = maxPlayers;
        players = new TreeMap<>();
        smallBlind = SMALL_BLIND;

        buttonIndex = -1;
    }

    public int getButtonIndex() {
        return buttonIndex;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSmallBlind() {
        return smallBlind;
    }

    public BigDecimal getBigBlind() {
        return smallBlind.multiply(BigDecimal.TWO);
    }

    public Player sitIn(Player player, int position) {
        if (position < 0 || position >= maxPlayers) {
            throw new InvalidPositionException(player.getExternalId(), position);
        }
        if (isFull()) {
            throw new MaxPlayersException(player.getExternalId(), maxPlayers);
        }
        if (players.containsKey(position)) {
            throw new ChosenSeatException(player.getExternalId(), position);
        }
        if (players.containsValue(player)) {
            throw new DuplicatePlayerException(player.getExternalId());
        }
        players.put(position, player);
        return player;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isFull() {
        return getPlayerCount() == maxPlayers;
    }

    public Hand getHand() {
        return hand;
    }

    public List<Player> getPlayers() {
        return List.copyOf(players.values());
    }

    public Player getPlayer(String playerId) {
        if (playerId == null) {
            return null;
        }
        return players.values().stream().filter(p -> p.getPlayerId().equals(playerId)).findFirst().orElse(null);
    }

    public boolean canStartNewHand() {
        return getPlayerCount() > 1 && (getHand() == null || getHand().isFinished());
    }

    public Hand newHand() {
        if (!canStartNewHand()) {
            throw new InvalidStartHandException(getId());
        }
        buttonIndex = ++buttonIndex % getPlayerCount();
        players.values().forEach(Player::resetNextHand);
        hand = new Hand(List.copyOf(players.values()), buttonIndex, SMALL_BLIND);
        return hand;
    }
}
