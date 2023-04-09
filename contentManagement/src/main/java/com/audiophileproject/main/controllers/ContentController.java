package com.audiophileproject.main.controllers;

import com.audiophileproject.main.dto.ContentDTO;
import com.audiophileproject.main.dto.ContentDTOMapper;
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
	private final ContentDTOMapper contentDTOMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ContentDTO createContent(
			@Valid @RequestBody ContentDTO contentDTO,
			@RequestHeader String userId
	) throws UnsupportedContentType {
		var content = Content.builder()
				.title(contentDTO.title())
				.url(contentDTO.url())
				.contentType(contentDTO.contentType())
				.length(contentDTO.length())
				.pubDate(contentDTO.pubDate())
				.description(contentDTO.description())
				.tags(contentDTO.tags())
				.build();

		var c = contentService.createContent(content, userId);
		logger.info("Creating a content: " + c);
		return contentDTOMapper.apply(c);
	}

	@PutMapping("/{id}")
	public ContentDTO updateContent(
			@PathVariable Long id,
			@Valid @RequestBody ContentDTO contentDTO,
			@RequestHeader String userId
	) {
		var content = Content.builder()
				.title(contentDTO.title())
				.url(contentDTO.url())
				.contentType(contentDTO.contentType())
				.length(contentDTO.length())
				.pubDate(contentDTO.pubDate())
				.description(contentDTO.description())
				.tags(contentDTO.tags())
				.build();
		var c = contentService.updateContent(id, content, userId);
		logger.info("Updating content {id=" + id + "}: " + c);
		return contentDTOMapper.apply(c);
	}

	@GetMapping("/{id}")
	public ContentDTO getContentById(
			@PathVariable Long id,
			@RequestHeader String userId
	) {
		return contentDTOMapper.apply(
				contentService.getContentById(id, userId)
		);
	}

	@GetMapping
	public List<ContentDTO> getAllContent(@RequestHeader String userId) {
		var contents = contentService.getAllContent(userId);
		return contents.stream()
				.map(contentDTOMapper)
				.collect(Collectors.toList());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteContentById(
			@PathVariable Long id,
			@RequestHeader String userId
	) {
		contentService.deleteContentById(id, userId);
	}
}
