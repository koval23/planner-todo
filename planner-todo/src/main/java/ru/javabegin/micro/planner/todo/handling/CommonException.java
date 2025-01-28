package ru.javabegin.micro.planner.todo.handling;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;
    private final List<String> params;
    private final HttpStatus status;

    public CommonException(ErrorCode errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
        this.params = List.of();
    }

    public CommonException(ErrorCode errorCode, HttpStatus status, List<String> params) {
        this.errorCode = errorCode;
        this.status = status;
        this.params = params;
    }


    private void logWarning(String errorCode, Locale locale) {
        System.err.printf("Message for error code '%s' not found for locale '%s'%n", errorCode, locale);
    }

    public static CommonException of(ErrorCode errorCode, HttpStatus status) {
        return new CommonException(errorCode, status);
    }

    public static CommonException of(ErrorCode errorCode, HttpStatus status, List<String> params) {
        return new CommonException(errorCode, status, params);
    }
}
