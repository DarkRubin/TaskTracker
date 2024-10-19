package org.roadmap.tasktrackerbackend.advice;

import lombok.extern.slf4j.Slf4j;
import org.roadmap.tasktrackerbackend.exception.BasicRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BasicRuntimeException.class)
    private ResponseEntity<Map<String, String>> sendErrorMessage(BasicRuntimeException ex) {
        return ResponseEntity.status(ex.getCode()).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Map<String, String>> sendErrorMessage(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(500).body(Map.of("message", "Unexpected error"));
    }
}
