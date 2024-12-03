package com.example.java_board.api.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record CreateBoard(
        @NotNull
        Long userId,
        @NotBlank
        String title,
        @NotBlank
        String content,
        List<String> tags
) {
}


