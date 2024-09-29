package org.roadmap.tasktrackerbackend.exception;

public class InvalidUserDetailsException extends BasicRuntimeException {
    public InvalidUserDetailsException() {
        super("Invalid input data", 400);
    }
}
