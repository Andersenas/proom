package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidAllInException extends ProomException {
    public InvalidAllInException(String playerId) {
        super(Map.of("playerId", playerId));
    }
}
