package com.example.java_board.repository.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.CommentFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.board.BoardRepository;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByIdAndBoardId() {
        User user = UserFixture.user();
        userRepository.save(user);

        Board board = BoardFixture.board(user);
        boardRepository.save(board);

        Comment comment = CommentFixture.comment(board, user);
        commentRepository.save(comment);

        assertTrue(commentRepository.existsByIdAndBoardId(comment.getId(), board.getId()));
    }


}