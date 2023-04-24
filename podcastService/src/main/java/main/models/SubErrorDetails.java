package main.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SubErrorDetails {
    private String field;
    private Object rejectedValue;
    private String message;
}
