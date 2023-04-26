package com.podapter.main.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    public List<SubErrorDetails> subErrors;

    public ErrorDetails(HttpStatus status) {
        this.status = status;
        timestamp = LocalDateTime.now();
    }

    public ErrorDetails(HttpStatus status, Throwable ex) {
        this(status, "Unexpected error", ex);
    }

    public ErrorDetails(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        timestamp = LocalDateTime.now();
    }
}
