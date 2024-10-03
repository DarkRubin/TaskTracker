package org.roadmap.tasktrackerbackend.exception;

public class TaskNotFoundException extends BasicRuntimeException{
    public TaskNotFoundException() {
        super("Task not found by this UUID", 400);
    }
}
