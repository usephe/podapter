package com.audiophileproject.main.controllers.advice;

import com.audiophileproject.main.exceptions.UnsupportedContentType;
import com.audiophileproject.main.models.ErrorDetails;
import com.audiophileproject.main.models.SubErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(UnsupportedContentType.class)
    public ResponseEntity<ErrorDetails> exceptionUnsupportedContentTypeHandler(UnsupportedContentType ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDetails(HttpStatus.BAD_REQUEST, "Unsupported content type", ex)
        );
    }
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

   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ResponseEntity<ErrorDetails> exceptionHttpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
       List<SubErrorDetails> subErrors = new ArrayList<>();
           subErrors.add(
                   new SubErrorDetails(
                           "field",
                           "value",
                           ex.getMessage()
                   )
           );


       var error = new ErrorDetails(HttpStatus.BAD_REQUEST, "Failed to read request", ex);
       error.setSubErrors(subErrors);
       return ResponseEntity.badRequest().body(error);
   }
}
