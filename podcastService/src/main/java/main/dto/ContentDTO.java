package main.dto;


import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public record ContentDTO(
		long id,
		String title,
		String url,
		String contentType,
		long length,
		Date pubDate,
		String description,
		List<String> tags
) {
}
