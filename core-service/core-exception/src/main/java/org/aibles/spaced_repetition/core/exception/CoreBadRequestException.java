package org.aibles.spaced_repetition.core.exception;

import org.springframework.http.HttpStatus;

public class CoreBadRequestException extends CoreException {
    
    public CoreBadRequestException(String errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
    }

    public CoreBadRequestException(String errorCode, Object... args) {
        super(errorCode, HttpStatus.BAD_REQUEST, args);
    }

    public CoreBadRequestException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.BAD_REQUEST);
    }

    public CoreBadRequestException(String errorCode, String message, Object... args) {
        super(errorCode, message, HttpStatus.BAD_REQUEST, args);
    }
}