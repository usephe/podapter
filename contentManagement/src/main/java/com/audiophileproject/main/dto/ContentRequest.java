package com.audiophileproject.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
	@NotBlank(message = "the title is required")
	private String title;
	@NotNull(message = "the url is required")
	private URL url;
	// TODO: validate the contentType
	private String contentType;
	private Long length;
	private Date pubDate;
	private String description;
	private List<String> tags;
}
