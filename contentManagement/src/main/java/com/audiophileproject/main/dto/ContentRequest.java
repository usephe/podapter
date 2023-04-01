package com.audiophileproject.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
	@NotBlank(message = "the title is required")
	private String title;
	@NotBlank(message = "the url is required")
	private String url;
	private String type;
	private Long length;
	private Date pubDate;
	private String description;
	private List<String> tags;
}
