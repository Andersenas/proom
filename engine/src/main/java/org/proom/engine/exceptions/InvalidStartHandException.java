package org.proom.engine.exceptions;

import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidStartHandException extends ProomException {
    public InvalidStartHandException(String boardId) {
        super(Map.of("boardId", boardId));
    }
}
