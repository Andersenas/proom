package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class ChosenSeatException extends ProomException {
    public ChosenSeatException(String externalId, int position) {
        super(Map.of("externalId", externalId, "position", position));
    }
}
