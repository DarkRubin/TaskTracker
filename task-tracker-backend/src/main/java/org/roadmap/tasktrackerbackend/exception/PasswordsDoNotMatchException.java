package org.roadmap.tasktrackerbackend.exception;

public class PasswordsDoNotMatchException extends BasicRuntimeException {
    public PasswordsDoNotMatchException() {
        super("Passwords do not match", 401);
    }
}
