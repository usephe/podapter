package com.audiophileproject.main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContentResponse (
	 long id,
	 String title,
	 String url,
	 String type,
	 Long length,
	 Date pubDate,
	 String description,
	 List<String> tags
) {
}
