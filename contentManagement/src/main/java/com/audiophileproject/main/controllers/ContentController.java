package com.audiophileproject.main.controllers;

import com.audiophileproject.main.dto.ContentRequest;
import com.audiophileproject.main.dto.ContentResponse;
import com.audiophileproject.main.dto.ContentResponseMapper;
import com.audiophileproject.main.exceptions.UnsupportedContentType;
import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.services.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {
	private final ContentService contentService;
	private final static Logger logger =
			Logger.getLogger(ContentController.class.getName());

	private final ContentResponseMapper contentResponseMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ContentResponse createContent(
			@Valid @RequestBody ContentRequest request,
			@RequestHeader("userId") String userId
	) throws UnsupportedContentType {
		var content = Content.builder()
				.title(request.getTitle())
				.url(request.getUrl())
				.contentType(request.getContentType())
				.length(request.getLength())
				.pubDate(request.getPubDate())
				.description(request.getDescription())
				.tags(request.getTags())
				.build();

		var c = contentService.createContent(content, userId);
		logger.info("Creating a content: " + c);
		return contentResponseMapper.apply(c);
	}

	@GetMapping("/{id}")
	public ContentResponse getContentById(@PathVariable Long id) {
		var content = contentService.getContentById(id);
		return contentResponseMapper.apply(content);
	}

	@GetMapping
	public List<ContentResponse> getAllContent(@RequestHeader("userId") String userId) {
		var contents = contentService.getAllContent(userId);
		return contents.stream()
				.map(contentResponseMapper)
				.collect(Collectors.toList());
	}

	@DeleteMapping("/{id}")
	public void deleteContentById(@PathVariable Long id) {
		contentService.deleteContentById(id);
	}
}
