package com.audiophileproject.main.controllers.advice;

import com.audiophileproject.main.exceptions.NoSpaceLeft;
import com.audiophileproject.main.exceptions.UnsupportedContentType;
import com.audiophileproject.main.models.ErrorDetails;
import com.audiophileproject.main.models.SubErrorDetails;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

   @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDetails> exceptionNoSuchElementHandler(NoSuchElementException ex) {
       var error = new ErrorDetails(HttpStatus.NOT_FOUND, "Element not found", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
   }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDetails> exceptionMultipartHandler(MultipartException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getMessage(), ex)
        );
    }

    @ExceptionHandler(NoSpaceLeft.class)
    public ResponseEntity<ErrorDetails> exceptionNoSpaceLeftHandler(NoSpaceLeft ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                new ErrorDetails(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage(), ex)
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> exceptionFeignHandler(FeignException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex)
        );
    }
}
