package com.audiophileproject.main.controllers;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.services.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {
	private final ContentService contentService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ContentResponse createContent(@Valid @RequestBody ContentRequest request) {
		var content = Content.builder()
				.title(request.getTitle())
				.description(request.getDescription())
				.fileUrl(request.getFileUrl())
				.build();
		var c = contentService.createContent(content);
		return ContentResponse.builder()
				.id(c.getId())
				.title(c.getTitle())
				.description(c.getDescription())
				.fileUrl(c.getFileUrl())
				.build();
	}

	@GetMapping("/{id}")
	public ContentResponse getContentById(@PathVariable Long id) {
		var content = contentService.getContentById(1L);
		return ContentResponse.builder()
				.id(content.getId())
				.title(content.getTitle())
				.description(content.getDescription())
				.fileUrl(content.getFileUrl())
				.build();
	}

	@GetMapping
	public List<ContentResponse> getAllContent() {
		var contents = contentService.getAllContent();
		return contents.stream()
				.map(
						content -> ContentResponse.builder()
								.id(content.getId())
								.title(content.getTitle())
								.description(content.getDescription())
								.fileUrl(content.getFileUrl())
								.build()
				)
				.collect(Collectors.toList());
	}

	@DeleteMapping("/{id}")
	public void deleteContentById(@PathVariable Long id) {
		contentService.deleteContentById(id);
	}
}
