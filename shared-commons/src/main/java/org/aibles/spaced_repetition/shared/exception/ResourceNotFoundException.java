package org.aibles.spaced_repetition.shared.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceId) {
        super("RESOURCE_NOT_FOUND", 
              String.format("Resource with id '%s' not found", resourceId), 
              HttpStatus.NOT_FOUND);
    }
}