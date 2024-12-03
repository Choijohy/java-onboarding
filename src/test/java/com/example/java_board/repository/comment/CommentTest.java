package com.example.java_board.repository.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.java_board.api.board.dto.request.UpdateComment;
import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.CommentFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    @DisplayName("Update DTO 로 update 되는 지 확인")
    void update() {
        // given
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = CommentFixture.comment(board, user);
        UpdateComment updateComment = new UpdateComment(user.getId(), board.getId(), "update content");

        // when
        comment.update(updateComment);

        // then
        assertEquals(updateComment.content(), comment.getContent());
    }

    @Test
    @DisplayName("like가 취소 되는 지 확인")
    void like_canceled() {
        // given
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = CommentFixture.comment(board, user);

        // when
        comment.like(user);
        comment.like(user);

        // then
        assertEquals(0, comment.getLikes().size());
    }

    @Test
    @DisplayName("like가 여러개 생기는 지 확인")
    void like_count() {
        // given
        User user = UserFixture.user();
        User otherUser = UserFixture.user("otherEmail");
        Board board = BoardFixture.board(user);
        Comment comment = CommentFixture.comment(board, user);

        // when
        comment.like(user);
        comment.like(otherUser);
        // then
        assertEquals(2, comment.getLikes().size());
    }
}