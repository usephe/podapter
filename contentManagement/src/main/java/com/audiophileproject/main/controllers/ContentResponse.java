package com.audiophileproject.main.controllers;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponse {
	private String title;
	private String description;
	private String fileUrl;
}
