package org.proom.server.services;

import org.proom.engine.game.Board;

/**
 * @author vasyalike
 */
public final class BoardChecker {

    private BoardChecker() { }

    public static void checkBoard(Board board, String boardId) {
        if (!board.getId().equals(boardId)) {
            throw new InvalidBoardException(boardId);
        }
    }
}
