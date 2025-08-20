package org.aibles.spaced_repetition.core.exception;

import org.springframework.http.HttpStatus;

public class CoreNotFoundException extends CoreException {
    
    public CoreNotFoundException(String errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }

    public CoreNotFoundException(String errorCode, Object... args) {
        super(errorCode, HttpStatus.NOT_FOUND, args);
    }

    public CoreNotFoundException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.NOT_FOUND);
    }

    public CoreNotFoundException(String errorCode, String message, Object... args) {
        super(errorCode, message, HttpStatus.NOT_FOUND, args);
    }
}