package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class DuplicatePlayerException extends ProomException {
    public DuplicatePlayerException(String externalId) {
        super(Map.of("externalId", externalId));
    }
}
