package com.example.java_board.api.user.exception;

import com.example.java_board._core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotFoundUserException extends ApiException {
    private static final String message = "유저를 찾을 수 없습니다.";

    public NotFoundUserException() {
        super(HttpStatus.NOT_FOUND, message);
    }
}
