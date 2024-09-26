package org.roadmap.tasktrackerbackend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasicRuntimeException extends RuntimeException {

    private final int code;

    protected BasicRuntimeException(String message, int code) {
        super(message);
        this.code = code;
    }
}
