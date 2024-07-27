package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidActionPlayerException extends ProomException {
    public InvalidActionPlayerException(String currentPlayerId, String invalidPlayerId) {
        super(Map.of("currentPlayerId", currentPlayerId, "invalidPlayerId", invalidPlayerId));
    }
}
