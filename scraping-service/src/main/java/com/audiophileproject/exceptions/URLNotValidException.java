package com.audiophileproject.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class URLNotValidException extends  RuntimeException{

    private String message;
}
