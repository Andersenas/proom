package org.proom.engine.exceptions;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author vasyalike
 */
public class NotEnoughChipsToBet extends ProomException {
    public NotEnoughChipsToBet(String playerId, BigDecimal chips, BigDecimal betAmount) {
        super(Map.of("playerId", playerId, "chips", chips, "betAmount", betAmount));
    }
}
