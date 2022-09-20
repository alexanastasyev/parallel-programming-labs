package org.example.integration;

public class InvalidLimitsException extends RuntimeException {
    public InvalidLimitsException() {
        super();
    }

    public InvalidLimitsException(String message) {
        super(message);
    }
}
