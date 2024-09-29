package org.roadmap.tasktrackerbackend.exception;

public class EmailAlreadyTakenException extends BasicRuntimeException {

    public EmailAlreadyTakenException() {
        super("This email is already taken", 409);
    }
}
