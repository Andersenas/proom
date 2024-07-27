package org.proom.server.controllers;

import org.proom.server.services.BoardData;
import org.proom.server.services.BoardService;
import org.proom.server.services.BoardState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author vasyalike
 */
@RestController
@RequestMapping("/boards")
public final class BoardsController {

    private final BoardService boardService;

    public BoardsController(BoardService boardService) {
        this.boardService = boardService;
    }

    public record Index(List<BoardData> boards) { }

    @GetMapping
    public Index index() {
        return new Index(boardService.getBoards());
    }

    public record BoardStateResponse(BoardState board) { }

    @GetMapping(value = {"/{boardId}", "/{boardId}/{playerId}"})
    public BoardStateResponse show(@PathVariable String boardId, @PathVariable(required = false) String playerId) {
        return new BoardStateResponse(boardService.getBoard(boardId, playerId));
    }

    public record SitInRequest(String externalId, int position) { }

    @PostMapping("/{boardId}/sit-in")
    public BoardStateResponse sitIn(@PathVariable String boardId, @RequestBody SitInRequest request) {
        return new BoardStateResponse(boardService.sitIn(boardId, request.position(), request.externalId()));
    }

    @PostMapping("/{boardId}/fold/{playerId}")
    public BoardStateResponse fold(@PathVariable String boardId, @PathVariable String playerId) {
        return new BoardStateResponse(boardService.fold(boardId, playerId));
    }

    @PostMapping("/{boardId}/call/{playerId}")
    public BoardStateResponse call(@PathVariable String boardId, @PathVariable String playerId) {
        return new BoardStateResponse(boardService.call(boardId, playerId));
    }

    @PostMapping("/{boardId}/check/{playerId}")
    public BoardStateResponse check(@PathVariable String boardId, @PathVariable String playerId) {
        return new BoardStateResponse(boardService.check(boardId, playerId));
    }
}
