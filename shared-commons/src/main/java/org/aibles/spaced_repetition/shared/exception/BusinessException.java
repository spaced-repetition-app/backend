package org.aibles.spaced_repetition.shared.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends BaseException {

    public BusinessException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, HttpStatus.BAD_REQUEST, cause);
    }
}