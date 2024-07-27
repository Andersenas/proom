package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidCallException extends ProomException {
    public InvalidCallException(String playerId) {
        super(Map.of("playerId", playerId));
    }
}
