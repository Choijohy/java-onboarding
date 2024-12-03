package com.example.java_board.repository.comment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByIdAndBoardId(long id, long boardId);

    Optional<Comment> findByIdAndBoardId(long id, long boardId);
}
