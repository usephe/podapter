package com.podapter.main.controllers.advice;

import com.podapter.main.models.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDetails> exceptionMultipartHandler(MultipartException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getMessage(), ex)
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDetails> exceptionNoSuchElementHandler(NoSuchElementException ex) {
        var error = new ErrorDetails(HttpStatus.NOT_FOUND, "File not found", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
