package org.roadmap.tasktrackerbackend.exception;

public class InvalidUserDetailsException extends BasicRuntimeException {
    public InvalidUserDetailsException(String message) {
        super(message, 400);
    }
}
