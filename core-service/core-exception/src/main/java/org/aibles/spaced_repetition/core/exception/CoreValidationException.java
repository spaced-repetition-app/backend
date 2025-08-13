package org.aibles.spaced_repetition.core.exception;

import org.springframework.http.HttpStatus;

public class CoreValidationException extends CoreException {
    
    public CoreValidationException(String errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
    }

    public CoreValidationException(String errorCode, Object... args) {
        super(errorCode, HttpStatus.BAD_REQUEST, args);
    }

    public CoreValidationException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.BAD_REQUEST);
    }

    public CoreValidationException(String errorCode, String message, Object... args) {
        super(errorCode, message, HttpStatus.BAD_REQUEST, args);
    }
}