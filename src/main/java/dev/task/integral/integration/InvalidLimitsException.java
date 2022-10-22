package dev.task.integral.integration;

public class InvalidLimitsException extends RuntimeException {
    public InvalidLimitsException() {
        super();
    }

    public InvalidLimitsException(String message) {
        super(message);
    }
}
