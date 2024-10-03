package org.roadmap.tasktrackerbackend.exception;

public class InvalidTokenException extends BasicRuntimeException {
    public InvalidTokenException() {
        super("Invalid JWT token", 400);
    }
}
