package org.roadmap.tasktrackerbackend.exception;

public class PasswordsDoNotMatchException extends BasicRuntimeException {
    public PasswordsDoNotMatchException(String message) {
        super(message, 401);
    }
}
