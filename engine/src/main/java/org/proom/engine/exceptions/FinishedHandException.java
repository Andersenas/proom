package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class FinishedHandException extends ProomException {
    public FinishedHandException(String playerId) {
        super(Map.of("playerId", playerId));
    }
}
