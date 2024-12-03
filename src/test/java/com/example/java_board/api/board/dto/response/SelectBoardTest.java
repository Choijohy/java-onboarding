package com.example.java_board.api.board.dto.response;

import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.TagFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.api.user.dto.response.SelectUser;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.tag.Tag;
import com.example.java_board.repository.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectBoardTest {

    @Test
    void successInitialized(){
        // Given
        SelectUser selectUser = UserFixture.selectUser();
        User user = UserFixture.user(selectUser.getEmail());
        Board board = BoardFixture.board(user);

        int likes = 100;
        List<String> tags = List.of("tag1","tag2");

        // When
        SelectBoard selectBoard = new SelectBoard(board, selectUser, likes, tags);

        // Then
        assertThat(selectBoard).isNotNull();
        assertThat(selectBoard.id()).isEqualTo(board.getId());
        assertThat(selectBoard.title()).isEqualTo(board.getTitle());
        assertThat(selectBoard.content()).isEqualTo(board.getContent());
        assertThat(selectBoard.likes()).isEqualTo(likes);
        assertThat(selectBoard.tags()).containsExactly(tags.toArray(new String[0]));
    }
}
