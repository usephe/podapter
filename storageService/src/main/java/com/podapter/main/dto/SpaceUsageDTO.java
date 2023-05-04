package com.podapter.main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.net.URL;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpaceUsageDTO(
        @NotNull
        Long usedSpace
) {
}
