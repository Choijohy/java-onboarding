package com.example.java_board.repository.board;

import com.example.java_board.api.board.dto.request.UpdateBoard;
import com.example.java_board.repository._common.IdentifiableEntity;
import com.example.java_board.repository.comment.Comment;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.example.java_board.repository.user.User;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends IdentifiableEntity {
    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = {CascadeType.REMOVE})
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.board", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<BoardLike> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.board", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<BoardTag> tags;

    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.comments = new ArrayList<>();
        this.likes = new HashSet<>();
        this.tags = new HashSet<>();
    }

    public void update(UpdateBoard updateBoard) {
        this.title = updateBoard.title();
        this.content = updateBoard.content();
    }

    public void updateTags(Set<BoardTag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public void like(User user) {
        BoardLike boardLike = new BoardLike(user, this);

        if (this.likes.contains(boardLike)) {
            this.likes.remove(boardLike);
        } else {
            this.likes.add(boardLike);
        }
    }
}
