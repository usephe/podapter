package com.audiophileproject.main.controllers;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponse {
	private long id;
	private String title;
	private String description;
	private String fileUrl;
}
