package org.aibles.spaced_repetition.core.exception;

import org.springframework.http.HttpStatus;

public class CoreInternalServerException extends CoreException {
    
    public CoreInternalServerException(String errorCode) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public CoreInternalServerException(String errorCode, Object... args) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR, args);
    }

    public CoreInternalServerException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public CoreInternalServerException(String errorCode, String message, Object... args) {
        super(errorCode, message, HttpStatus.INTERNAL_SERVER_ERROR, args);
    }
}