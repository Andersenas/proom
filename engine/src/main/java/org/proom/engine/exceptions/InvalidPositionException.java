package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidPositionException extends ProomException {
    public InvalidPositionException(String externalId, int position) {
        super(Map.of("externalId", externalId, "invalidPosition", position));
    }
}
