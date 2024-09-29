package org.roadmap.tasktrackerbackend.exception;

public class UserNotAuthorizedException extends BasicRuntimeException {
  public UserNotAuthorizedException() {
    super("User not authorized or token is expired", 401);
  }
}
