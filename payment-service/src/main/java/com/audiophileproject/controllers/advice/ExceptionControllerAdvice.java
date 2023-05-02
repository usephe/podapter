package com.audiophileproject.controllers.advice;

import com.audiophileproject.models.ErrorDetails;
import com.audiophileproject.models.SubErrorDetails;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> exceptionMethodArgumentValidHandler(MethodArgumentNotValidException ex) {
        List<SubErrorDetails> subErrors = new ArrayList<>();
        for (var fieldError: ex.getBindingResult().getFieldErrors())
            subErrors.add(
                    new SubErrorDetails(
                            fieldError.getField(),
                            fieldError.getRejectedValue(),
                            fieldError.getDefaultMessage()
                    )
            );

        var error = new ErrorDetails(HttpStatus.BAD_REQUEST, "Invalid arguments", ex);
        error.setSubErrors(subErrors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> exceptionMethodArgumentTypeMismatchHandler(MethodArgumentTypeMismatchException ex) {
        var message = "Failed to read argument: `" + ex.getName() + "`";
        var error = new ErrorDetails(HttpStatus.BAD_REQUEST, message , ex);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDetails> exceptionMissingServletRequestParameterHandler(MissingServletRequestParameterException ex) {
        var message = "Missing request parameter(s)";
        var error = new ErrorDetails(HttpStatus.BAD_REQUEST, message , ex);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> exceptionIllegalArgumentHandler(IllegalArgumentException ex) {
        var message = ex.getMessage() != null ? ex.getMessage() : "Illegal argument";
        var error = new ErrorDetails(HttpStatus.BAD_REQUEST, message , ex);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignException(FeignException ex) {
        var message = "User not found";
        var error = new ErrorDetails(HttpStatus.valueOf(ex.status()), message , ex);
        return ResponseEntity.badRequest().body(error);
    }

}
