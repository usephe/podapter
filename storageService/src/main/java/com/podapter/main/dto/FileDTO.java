package com.podapter.main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.net.URL;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileDTO(
        Long id,
        String fileName,
        Long fileSize,
        String fileType,
        URL url
) {
}
