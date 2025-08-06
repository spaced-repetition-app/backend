package org.aibles.spaced_repetition.shared.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException() {
        super("UNAUTHORIZED", "Access denied. Authentication required.", HttpStatus.UNAUTHORIZED);
    }
}