package dev.task.integral.integration;

public class InvalidLimitsException extends RuntimeException {
    @SuppressWarnings("unused")
    public InvalidLimitsException() {
        super();
    }

    public InvalidLimitsException(String message) {
        super(message);
    }
}
