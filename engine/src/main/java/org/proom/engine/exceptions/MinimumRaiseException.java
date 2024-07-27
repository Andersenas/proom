package org.proom.engine.exceptions;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author vasyalike
 */
public class MinimumRaiseException extends ProomException {
    public MinimumRaiseException(BigDecimal minBet, BigDecimal amount, String playerId) {
        super(Map.of("minBet", minBet, "betAmount", amount, "playerId", playerId));
    }
}
