package com.example.java_board.api.board.dto.response;

import com.example.java_board.api.user.dto.response.SelectUser;
import com.example.java_board.repository.board.Board;
import com.querydsl.core.annotations.QueryProjection;

import java.util.List;

public record SelectBoard(
        long id,
        String title,
        String content,
        SelectUser user,
        int likes,
        List<String> tags
) {

    @QueryProjection
    public SelectBoard(
            Board board,
            SelectUser user,
            int likes,
            List<String> tags
    ) {
        this(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                user,
                likes,
                tags
        );
    }
}
