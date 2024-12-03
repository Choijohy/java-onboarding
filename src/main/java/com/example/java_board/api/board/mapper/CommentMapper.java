package com.example.java_board.api.board.mapper;

import com.example.java_board.api.board.dto.request.CreateComment;
import com.example.java_board.api.board.exception.NotFoundBoardException;
import com.example.java_board.api.user.exception.NotFoundUserException;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.board.BoardRepository;
import com.example.java_board.repository.comment.Comment;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public Comment toEntity(long boardId, CreateComment createComment) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundBoardException::new);
        User user = userRepository.findById(createComment.userId()).orElseThrow(NotFoundUserException::new);
        return new Comment(createComment.content(), board, user);
    }

}
