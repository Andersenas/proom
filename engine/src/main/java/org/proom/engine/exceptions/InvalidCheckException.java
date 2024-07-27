package org.proom.engine.exceptions;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidCheckException extends ProomException {
    public InvalidCheckException(String playerId, BigDecimal toBet) {
        super(Map.of("playerId", playerId, "toBet", toBet));
    }
}
