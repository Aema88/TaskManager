package com.task.rest.taskmanagerrest.controller;

import com.task.rest.exception.InvalidStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<String> handleInvalidStatusException(InvalidStatusException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}

