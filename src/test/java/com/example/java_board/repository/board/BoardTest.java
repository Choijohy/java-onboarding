package com.example.java_board.repository.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.java_board.api.board.dto.request.UpdateBoard;
import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.repository.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void update() {
        // given
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        UpdateBoard updateBoard = new UpdateBoard(user.getId(), "update title", "update content", null);

        // when
        board.update(updateBoard);

        // then
        assertEquals(updateBoard.title(), board.getTitle());
        assertEquals(updateBoard.content(), board.getContent());
    }

    @Test
    @DisplayName("like가 취소 되는 지 확인")
    void like_canceled() {
        // given
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);

        // when
        board.like(user);
        board.like(user);

        // then
        assertEquals(0, board.getLikes().size());
    }

    @Test
    @DisplayName("like가 여러개 생기는 지 확인")
    void like_count() {
        // given
        User user = UserFixture.user();
        User otherUser = UserFixture.user("otherEmail");
        Board board = BoardFixture.board(user);

        // when
        board.like(user);
        board.like(otherUser);

        // then
        assertEquals(2, board.getLikes().size());
    }
}