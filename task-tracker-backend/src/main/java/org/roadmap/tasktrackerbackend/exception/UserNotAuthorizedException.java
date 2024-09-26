package org.roadmap.tasktrackerbackend.exception;

public class UserNotAuthorizedException extends BasicRuntimeException {
  public UserNotAuthorizedException(String message) {
    super(message, 401);
  }
}
