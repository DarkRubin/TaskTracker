package org.roadmap.tasktrackerbackend.exception;

public class UserNotFoundException extends BasicRuntimeException {
    public UserNotFoundException(String message) {
        super(message, 409);
    }
}
