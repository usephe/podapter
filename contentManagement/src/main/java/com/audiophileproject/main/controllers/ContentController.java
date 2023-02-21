package com.audiophileproject.main.controllers;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.services.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {
	private final ContentService contentService;

	@PostMapping
	public ContentResponse createContent(@Valid @RequestBody ContentRequest request) {
		var content = Content.builder()
				.title(request.getTitle())
				.description(request.getDescription())
				.fileUrl(request.getFileUrl())
				.build();
		var c = contentService.createContent(content);
		return ContentResponse.builder()
				.title(c.getTitle())
				.description(c.getDescription())
				.fileUrl(c.getFileUrl())
				.build();
	}

	@GetMapping("/{id}")
	public Content getContentById(@PathVariable Long id) {
		return contentService.getContentById(1L);
	}

	@GetMapping
	public List<Content> getAllContent() {
		return contentService.getAllContent();
	}

	@DeleteMapping("/{id}")
	public void deleteContentById(@PathVariable Long id) {
		contentService.deleteContentById(id);
	}
}
