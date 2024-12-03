package com.example.java_board.api.board;

import com.example.java_board._core.dto.ApiResponse;
import com.example.java_board.api.board.dto.request.CreateComment;
import com.example.java_board.api.board.dto.request.UpdateComment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{boardId}/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createComment(
            @PathVariable long boardId,
            @RequestBody @Valid CreateComment createComment
    ) {
        commentService.create(boardId, createComment);
        return ApiResponse.create();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @PathVariable long boardId,
            @PathVariable long commentId,
            @RequestBody @Valid UpdateComment updateComment
    ) {
        commentService.update(boardId, commentId, updateComment);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable long boardId,
            @PathVariable long commentId
    ) {
        commentService.delete(boardId, commentId);
        return ApiResponse.ok();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<Void>> commentLike(
            @PathVariable long boardId,
            @PathVariable long commentId,
            @RequestParam long userId
    ) {
        commentService.like(boardId, commentId, userId);
        return ApiResponse.ok();
    }

}
