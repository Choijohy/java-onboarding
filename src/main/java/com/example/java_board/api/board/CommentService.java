package com.example.java_board.api.board;

import com.example.java_board.api.board.dto.request.CreateComment;
import com.example.java_board.api.board.dto.request.UpdateComment;
import com.example.java_board.api.board.exception.NotFoundCommentException;
import com.example.java_board.api.board.mapper.CommentMapper;
import com.example.java_board.api.user.exception.NotFoundUserException;
import com.example.java_board.repository.comment.Comment;
import com.example.java_board.repository.comment.CommentRepository;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public void create(long boardId, CreateComment createComment) {
        Comment comment = commentMapper.toEntity(boardId, createComment);
        commentRepository.save(comment);
    }

    @Transactional
    public void update(long boardId, long commentId, UpdateComment updateComment) {
        Comment comment = commentRepository.findByIdAndBoardId(commentId, boardId).orElseThrow(NotFoundCommentException::new);
        comment.update(updateComment);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(long boardId, long commentId) {
        if (!commentRepository.existsByIdAndBoardId(commentId, boardId)) {
            throw new NotFoundCommentException();
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void like(long boardId, long commentId, long userId) {
        Comment comment = commentRepository.findByIdAndBoardId(commentId, boardId).orElseThrow(NotFoundCommentException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        comment.like(user);
        commentRepository.save(comment);
    }

}