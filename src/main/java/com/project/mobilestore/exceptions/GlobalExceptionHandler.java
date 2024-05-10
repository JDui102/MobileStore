package com.project.mobilestore.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Catch RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public String handleDataIntegrityViolationException(RuntimeException ex) {
        // Return message
        return ex.getMessage();
    }

    // Catch handleIllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        // Return message
        return ex.getMessage();
    }
}


