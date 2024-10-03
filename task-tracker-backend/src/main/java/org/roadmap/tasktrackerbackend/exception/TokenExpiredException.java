package org.roadmap.tasktrackerbackend.exception;

public class TokenExpiredException extends BasicRuntimeException {
    public TokenExpiredException() {
        super("Authentication token is expired, login for get new token", 401);
    }
}
