package com.example.java_board.repository.board;

import com.example.java_board.repository.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardTag {

    @EmbeddedId
    private BoardTagId id;

    public BoardTag(Board board, Tag tag) {
        this.id = new BoardTagId(board, tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardTag boardTag)) return false;
        return Objects.equals(id, boardTag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class BoardTagId implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "board_id", nullable = false)
        private Board board;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tag_id", nullable = false)
        private Tag tag;

        public BoardTagId(Board board, Tag tag) {
            this.board = board;
            this.tag = tag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BoardTag.BoardTagId that)) return false;
            return Objects.equals(board, that.board) && Objects.equals(tag, that.tag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(board, tag);
        }
    }
}
