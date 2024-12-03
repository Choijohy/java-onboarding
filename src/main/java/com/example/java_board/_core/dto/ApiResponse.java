package com.example.java_board._core.dto;

import com.example.java_board._core.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiResponse<T>(
        boolean error,
        String message,
        T data
) {
    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, null, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, null, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failedOf(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(true, message, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failedOf(ApiException e) {
        return ResponseEntity.status(e.status).body(new ApiResponse<>(true, e.getMessage(), null));
    }

    // ------
    // ETC
    // ------

    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ApiResponse.of(HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> create() {
        return ApiResponse.of(HttpStatus.CREATED);
    }
}
