package org.application.ecomappbe.exception;

public class ResourceAccessDeniedException extends RuntimeException {
    public ResourceAccessDeniedException(String message) {
        super(message);
    }
}
