package org.aibles.spaced_repetition.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CoreException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] args;

    public CoreException(String errorCode, HttpStatus httpStatus) {
        this(errorCode, httpStatus, (Object[]) null);
    }

    public CoreException(String errorCode, HttpStatus httpStatus, Object... args) {
        super(errorCode);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public CoreException(String errorCode, String message, HttpStatus httpStatus) {
        this(errorCode, message, httpStatus, (Object[]) null);
    }

    public CoreException(String errorCode, String message, HttpStatus httpStatus, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }
}