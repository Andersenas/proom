package org.proom.server.services;

import org.proom.engine.exceptions.ProomException;

import java.util.Map;

/**
 * @author vasyalike
 */
public class InvalidBoardException extends ProomException {
    public InvalidBoardException(String boardId) {
        super(Map.of("boardId", boardId));
    }
}
