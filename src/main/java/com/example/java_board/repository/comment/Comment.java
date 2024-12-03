package com.example.java_board.repository.comment;

import com.example.java_board.api.board.dto.request.UpdateComment;
import com.example.java_board.repository._common.IdentifiableEntity;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.user.User;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;

import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends IdentifiableEntity {
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id"})
    )
    private Set<User> likes;

    public Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
        this.likes = new HashSet<>();
    }

    public void update(UpdateComment updateComment) {
        this.content = updateComment.content();
    }

    public void like(User user) {
        if (this.likes.contains(user)) {
            this.likes.remove(user);
        } else {
            this.likes.add(user);
        }
    }
}
