package com.example.java_board.api.board;


import com.example.java_board._core.dto.ApiResponse;
import com.example.java_board.api.board.dto.request.*;
import com.example.java_board.api.board.dto.response.SelectBoard;
import com.example.java_board.repository.board.Board;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<SelectBoard>> getBoardById(
            @PathVariable long boardId
    ){
        Optional<SelectBoard> selectBoard = boardService.findBoardById(boardId);

        if (selectBoard.isPresent()) {
            return ApiResponse.of(HttpStatus.OK,selectBoard.get());
        } else{
            return ApiResponse.of(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBoard(
            @RequestBody @Valid CreateBoard createBoard
    ) {
        boardService.create(createBoard);
        return ApiResponse.create();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable long boardId,
            @RequestBody @Valid UpdateBoard updateBoard
    ) {
        boardService.update(boardId, updateBoard);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable long boardId
    ) {
        boardService.delete(boardId);
        return ApiResponse.ok();
    }

    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> like(
            @PathVariable long boardId,
            @RequestParam long userId
    ) {
        boardService.like(boardId, userId);
        return ApiResponse.ok();
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<SelectBoard>>> getBoards(
            @PageableDefault Pageable pageable
    ) {
        return ApiResponse.of(HttpStatus.OK, boardService.findBoards(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SelectBoard>>> getBoardsSearch(
            @Valid SearchCondition searchCondition,
            @PageableDefault Pageable pageable
    ) {
        return ApiResponse.of(HttpStatus.OK, boardService.findBoardsByCondition(searchCondition, pageable));
    }

    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<Page<SelectBoard>>> getBoardSort(
            @Valid SortCondition sortCondition,
            int page_no,
            int size

    ){
        Pageable pageable = PageRequest.of(page_no,size);
        return ApiResponse.of(HttpStatus.OK, boardService.findSortedBoards(sortCondition, pageable));
    }
}
