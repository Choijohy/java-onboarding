package com.example.java_board.api.board.exception;

import com.example.java_board._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotFoundBoardException extends ApiException {
    private static final String message = "게시글을 찾을 수 없습니다.";

    public NotFoundBoardException() {
        super(HttpStatus.NOT_FOUND, message);
    }
}