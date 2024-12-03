package com.example.java_board.repository.board;

import com.example.java_board.repository.user.User;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLike {

    @EmbeddedId
    private BoardLikeId id;

    public BoardLike(User user, Board board) {
        this.id = new BoardLikeId(board, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardLike boardLike)) return false;
        return Objects.equals(id, boardLike.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class BoardLikeId implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "board_id", nullable = false)
        private Board board;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        public BoardLikeId(Board board, User user) {
            this.board = board;
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BoardLikeId that)) return false;
            return Objects.equals(board, that.board) && Objects.equals(user, that.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(board, user);
        }
    }
}

