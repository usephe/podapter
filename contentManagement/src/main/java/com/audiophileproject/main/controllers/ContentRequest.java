package com.audiophileproject.main.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
	@NotBlank(message = "the title is required")
	private String title;
	@NotBlank(message = "the description is required")
	private String description;
	@NotBlank(message = "the fileUrl is required")
	private String fileUrl;
}
