package ru.javabegin.micro.planner.users.exception.handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Object> handleCommonException(CommonException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of(
                        "errorCode", ex.getErrorCode().name(),
                        "message", ex.getLocalizedMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(Map.of(
                        "error", "Unexpected error",
                        "details", ex.getMessage()
                ));
    }
}

