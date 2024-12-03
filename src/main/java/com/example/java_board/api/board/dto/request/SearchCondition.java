package com.example.java_board.api.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SearchCondition(
        @NotNull
        SearchType type,
        @NotBlank
        String keyword
) {
}
