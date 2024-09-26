package org.roadmap.tasktrackerbackend.exception;

public class EmailAlreadyTakenException extends BasicRuntimeException {

    public EmailAlreadyTakenException(String message) {
        super(message, 409);
    }
}
