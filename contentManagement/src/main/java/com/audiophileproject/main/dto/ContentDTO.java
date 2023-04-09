package com.audiophileproject.main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContentDTO (
    Long id,
    @NotBlank(message = "the title is required")
    String title,
    @NotNull(message = "the url is required")
    URL url,
    String contentType,
    Long length,
    Date pubDate,
    String description,
    List<String> tags
) {
}
