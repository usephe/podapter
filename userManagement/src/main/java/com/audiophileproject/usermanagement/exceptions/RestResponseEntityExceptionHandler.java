package com.audiophileproject.usermanagement.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = { UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException e, WebRequest request){

        return new ResponseEntity<>(
          "User not found",
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    protected ResponseEntity<Object> handleUnauthenticatedException(UserPrincipalNotFoundException e, WebRequest request){

        return new ResponseEntity<>(
                "User not found",
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    protected ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException e, WebRequest request){
        return new ResponseEntity<>(
                "Token expired",
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    protected ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException e, WebRequest request){
        return new ResponseEntity<>(
                "Invalid token",
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED
        );
    }


    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e, WebRequest request){
        return new ResponseEntity<>(
                "Bad Credentials",
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException e, WebRequest request){
        return new ResponseEntity<>(
                "Authentication failed",
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInternalErrorException(Exception e, WebRequest request){
        return new ResponseEntity<>(
                "Something went wrong",
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    };



}
