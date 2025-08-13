package org.aibles.spaced_repetition.shared.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName, String resourceId) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s with id '%s' not found", resourceName, resourceId), 
              HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
}