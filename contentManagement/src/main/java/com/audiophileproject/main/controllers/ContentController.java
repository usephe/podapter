package com.audiophileproject.main.controllers;

import com.audiophileproject.main.dto.ContentDTO;
import com.audiophileproject.main.dto.ContentDTOMapper;
import com.audiophileproject.main.dto.ContentMapper;
import com.audiophileproject.main.exceptions.NoSpaceLeft;
import com.audiophileproject.main.exceptions.UnsupportedContentType;
import com.audiophileproject.main.services.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
public class ContentController {
	private final ContentService contentService;
	private final static Logger logger =
			Logger.getLogger(ContentController.class.getName());
	private final ContentDTOMapper contentDTOMapper;
	private final ContentMapper contentMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ContentDTO createContent(
			@Valid @RequestBody ContentDTO contentDTO,
			@RequestHeader String userId
	) throws UnsupportedContentType {
		var c = contentService.createContent(contentMapper.apply(contentDTO), userId);
		logger.info("Creating a content: " + c);
		return contentDTOMapper.apply(c);
	}

	@PostMapping("/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public ContentDTO store(
			@RequestParam("file") MultipartFile file,
			@RequestHeader String userId
	) throws UnsupportedContentType, NoSpaceLeft, MalformedURLException {

		var storedContent = contentService.createContent(file, userId);
		logger.info("Creating a content: " + storedContent);
		return contentDTOMapper.apply(storedContent);
	}

	@PutMapping("/{id}")
	public ContentDTO updateContent(
			@PathVariable Long id,
			@Valid @RequestBody ContentDTO contentDTO,
			@RequestHeader String userId
	) throws UnsupportedContentType {
		var c = contentService.updateContent(id, contentMapper.apply(contentDTO), userId);
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

	@GetMapping("/tags")
	public Set<String> getAllTags(@RequestHeader String userId) {
		return contentService.getAllTags(userId);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteContentById(
			@PathVariable Long id,
			@RequestHeader String userId
	) {
		if (contentService.deleteContentById(id, userId) == 0)
			throw new NoSuchElementException();
	}
}
