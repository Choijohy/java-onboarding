package com.example.java_board._test_utils.fixture;

import com.example.java_board.api.board.dto.request.CreateComment;
import com.example.java_board.api.board.dto.request.UpdateComment;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.comment.Comment;
import com.example.java_board.repository.user.User;

public class CommentFixture {

    public static Comment comment(Board board, User user) {
        return new Comment("content", board, user);
    }

    public static CreateComment createComment() {
        return new CreateComment(1L, "content");
    }

    public static UpdateComment updateComment() {
        return new UpdateComment(1L, 1L, "update content");
    }
}
