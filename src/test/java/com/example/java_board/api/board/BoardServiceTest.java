package com.example.java_board.api.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.java_board.api.board.dto.request.CreateBoard;
import com.example.java_board.api.board.dto.request.UpdateBoard;
import com.example.java_board.api.board.exception.NotFoundBoardException;
import com.example.java_board.api.board.mapper.BoardMapper;
import com.example.java_board.api.user.exception.NotFoundUserException;
import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.board.BoardRepository;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    BoardMapper boardMapper;

    @InjectMocks
    BoardService boardService;

    @Test
    void create() {
        // given
        User user = UserFixture.user();
        Board board = BoardFixture.board(user);
        CreateBoard createBoard = BoardFixture.createBoard();
        when(boardMapper.toEntity(createBoard)).thenReturn(board);
        when(boardRepository.save(board)).thenReturn(board);

        // when
        boardService.create(createBoard);

        // then
        verify(boardMapper).toEntity(createBoard);
        verify(boardRepository).save(board);
    }

    @Test
    void update() {
        // given
        long boardId = 1L;
        User user = UserFixture.user();
        Board board = spy(BoardFixture.board(user));
        UpdateBoard updateBoard = BoardFixture.updateBoard();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when
        boardService.update(boardId, updateBoard);

        // then
        verify(boardRepository).findById(boardId);
        verify(board).update(updateBoard);
        verify(boardRepository).save(board);
    }

    @Test
    void update_NotFoundBoardException() {
        // given
        long boardId = 1L;
        User user = UserFixture.user();
        Board board = spy(BoardFixture.board(user));
        UpdateBoard updateBoard = BoardFixture.updateBoard();
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundBoardException.class, () -> boardService.update(boardId, updateBoard));

        verify(boardRepository).findById(boardId);
        verify(board, never()).update(updateBoard);
        verify(boardRepository, never()).save(board);
    }

    @Test
    void delete() {
        // given
        long boardId = 1L;
        when(boardRepository.existsById(boardId)).thenReturn(true);

        // when
        boardService.delete(boardId);

        // then
        verify(boardRepository).existsById(boardId);
        verify(boardRepository).deleteById(boardId);
    }

    @Test
    void delete_NotFoundBoardException() {
        // given
        long boardId = 1L;
        when(boardRepository.existsById(boardId)).thenReturn(false);

        // when & then
        assertThrows(NotFoundBoardException.class, () -> boardService.delete(boardId));

        verify(boardRepository).existsById(boardId);
        verify(boardRepository, never()).deleteById(boardId);
    }

    @Test
    void like() {
        // given
        long userId = 1L;
        long boardId = 1L;
        User user = UserFixture.user();
        Board board = spy(BoardFixture.board(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when
        boardService.like(userId, boardId);

        // then
        verify(boardRepository).findById(boardId);
        verify(userRepository).findById(userId);
        verify(board).like(user);
        verify(boardRepository).save(board);
    }

    @Test
    void like_NotFoundBoardException() {
        // given
        long userId = 1L;
        long boardId = 1L;
        User user = UserFixture.user();
        Board board = spy(BoardFixture.board(user));
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // exception 발생으로 인해 이 메소드는 실행되지 않는다.
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundBoardException.class, () -> boardService.like(userId, boardId));

        verify(boardRepository).findById(boardId);
        verify(userRepository, never()).findById(userId);
        verify(board, never()).like(user);
        verify(boardRepository, never()).save(board);
    }

    @Test
    void like_NotFoundUserException() {
        // given
        long userId = 1L;
        long boardId = 1L;
        User user = UserFixture.user();
        Board board = spy(BoardFixture.board(user));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when & then
        assertThrows(NotFoundUserException.class, () -> boardService.like(userId, boardId));

        verify(boardRepository).findById(boardId);
        verify(userRepository).findById(userId);
        verify(board, never()).like(user);
        verify(boardRepository, never()).save(board);
    }
}