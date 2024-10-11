package org.roadmap.tasktrackerbackend.exception;

public class InvalidDateParameterException extends BasicRuntimeException {
    public InvalidDateParameterException() {
        super("Invalid date parameter", 400);
    }

}
