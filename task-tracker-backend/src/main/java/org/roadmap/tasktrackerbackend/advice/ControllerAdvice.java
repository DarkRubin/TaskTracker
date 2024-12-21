package org.roadmap.tasktrackerbackend.advice;

import lombok.extern.slf4j.Slf4j;
import org.roadmap.tasktrackerbackend.exception.BasicRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BasicRuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleBasicRuntimeException(BasicRuntimeException ex) {
        return ResponseEntity.status(ex.getCode())
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(Exception ex) {
        return ResponseEntity.status(400)
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(new ExceptionResponse(ex.getMessage()));
    }
}
