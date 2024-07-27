package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class EmptyDeckException extends ProomException {
    public EmptyDeckException() {
        super(Map.of());
    }
}
