package com.example.java_board.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUser(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
