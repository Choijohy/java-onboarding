package com.example.java_board.api.board.mapper;

import com.example.java_board.api.board.dto.request.CreateBoard;
import com.example.java_board.api.user.exception.NotFoundUserException;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final UserRepository userRepository;

    public Board toEntity(CreateBoard createBoard) {
        User user = userRepository.findById(createBoard.userId()).orElseThrow(NotFoundUserException::new);
        return new Board(createBoard.title(), createBoard.content(), user);
    }
}
