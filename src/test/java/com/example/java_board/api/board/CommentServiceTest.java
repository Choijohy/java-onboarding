package com.example.java_board.api.board;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.java_board.api.board.dto.request.CreateComment;
import com.example.java_board.api.board.dto.request.UpdateComment;
import com.example.java_board.api.board.exception.NotFoundCommentException;
import com.example.java_board.api.board.mapper.CommentMapper;
import com.example.java_board.api.user.exception.NotFoundUserException;
import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.CommentFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.comment.Comment;
import com.example.java_board.repository.comment.CommentRepository;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentMapper commentMapper;

    @InjectMocks
    CommentService commentService;

    @Test
    void create() {
        // given
        long boardId = 1L;
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = CommentFixture.comment(board, user);
        CreateComment createComment = CommentFixture.createComment();
        when(commentMapper.toEntity(boardId, createComment)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);

        // when
        commentService.create(boardId, createComment);

        // then
        verify(commentMapper).toEntity(boardId, createComment);
        verify(commentRepository).save(comment);
    }

    @Test
    void update() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = spy(CommentFixture.comment(board, user));
        UpdateComment updateComment = CommentFixture.updateComment();
        when(commentRepository.findByIdAndBoardId(commentId, boardId)).thenReturn(Optional.of(comment));

        // when
        commentService.update(boardId, commentId, updateComment);

        // then
        verify(commentRepository).findByIdAndBoardId(commentId, boardId);
        verify(comment).update(updateComment);
        verify(commentRepository).save(comment);
    }

    @Test
    void update_NotFoundCommentException() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = spy(CommentFixture.comment(board, user));
        UpdateComment updateComment = CommentFixture.updateComment();
        when(commentRepository.findByIdAndBoardId(commentId, boardId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundCommentException.class, () -> commentService.update(boardId, commentId, updateComment));

        // then
        verify(commentRepository).findByIdAndBoardId(commentId, boardId);
        verify(comment, never()).update(updateComment);
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void delete() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        when(commentRepository.existsByIdAndBoardId(boardId, commentId)).thenReturn(true);

        // when
        commentService.delete(boardId, commentId);

        // then
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void delete_NotFoundCommentException() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        when(commentRepository.existsByIdAndBoardId(boardId, commentId)).thenReturn(false);

        // when
        assertThrows(NotFoundCommentException.class, () -> commentService.delete(boardId, commentId));

        // then
        verify(commentRepository, never()).deleteById(commentId);
    }

    @Test
    void like() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        long userId = 1L;
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = spy(CommentFixture.comment(board, user));
        when(commentRepository.findByIdAndBoardId(commentId, boardId)).thenReturn(Optional.of(comment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        commentService.like(boardId, commentId, userId);

        // then
        verify(commentRepository).findByIdAndBoardId(commentId, boardId);
        verify(userRepository).findById(userId);
        verify(comment).like(user);
        verify(commentRepository).save(comment);
    }

    @Test
    void like_NotFoundCommentException() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        long userId = 1L;
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = spy(CommentFixture.comment(board, user));
        when(commentRepository.findByIdAndBoardId(commentId, boardId)).thenReturn(Optional.empty());
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThrows(NotFoundCommentException.class, () -> commentService.like(boardId, commentId, userId));

        verify(commentRepository).findByIdAndBoardId(commentId, boardId);
        verify(userRepository, never()).findById(userId);
        verify(comment, never()).like(user);
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void like_NotFoundUserException() {
        // given
        long boardId = 1L;
        long commentId = 1L;
        long userId = 1L;
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        Comment comment = spy(CommentFixture.comment(board, user));
        when(commentRepository.findByIdAndBoardId(commentId, boardId)).thenReturn(Optional.of(comment));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundUserException.class, () -> commentService.like(boardId, commentId, userId));

        // then
        verify(commentRepository).findByIdAndBoardId(commentId, boardId);
        verify(userRepository).findById(userId);
        verify(comment, never()).like(user);
        verify(commentRepository, never()).save(comment);
    }
}