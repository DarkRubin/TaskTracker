package org.roadmap.tasktrackerbackend.exception;

public class UserNotFoundException extends BasicRuntimeException {
    public UserNotFoundException() {
        super("User not found", 409);
    }
}
