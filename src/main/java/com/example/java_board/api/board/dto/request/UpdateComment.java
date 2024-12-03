package com.example.java_board.api.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateComment(
        @NotNull
        Long userId,
        @NotNull
        Long boardId,
        @NotBlank
        String content
) {
}
