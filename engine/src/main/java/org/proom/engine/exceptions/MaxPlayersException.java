package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class MaxPlayersException extends ProomException {
    public MaxPlayersException(String externalId, int totalPlayers) {
        super(Map.of("externalId", externalId, "totalPlayers", totalPlayers));
    }
}
