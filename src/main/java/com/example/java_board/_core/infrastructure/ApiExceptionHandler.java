package com.example.java_board._core.infrastructure;

import com.example.java_board._core.dto.ApiResponse;
import com.example.java_board._core.exception.ApiException;
import java.util.stream.Collectors;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    // Custom Api Exception
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        return ApiResponse.failedOf(e);
    }

    // ValidException Parsing
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + (fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : ""))
                .collect(Collectors.joining(", "));

        return ApiResponse.failedOf(HttpStatus.BAD_REQUEST, message);
    }

    // Request Not Readable
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ApiResponse.failedOf(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // UnHandled Exception
    @Order
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> unHandleException(Exception e) {
        e.printStackTrace();
        return ApiResponse.failedOf(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
