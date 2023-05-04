package com.audiophileproject.main.services;

import com.audiophileproject.main.exceptions.NoSpaceLeft;
import com.audiophileproject.main.exceptions.UnsupportedContentType;
import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.repositories.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
@AllArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final StorageService storageService;

    public Content createContent(Content content, String userId) throws UnsupportedContentType {
        content.setUserId(userId);
        if (content.getPubDate() == null)
            content.setPubDate(new Date());
        URL url = content.getUrl();

        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (content.getContentType() == null) {
            String contentType = connection.getContentType();
            content.setContentType(contentType);
        }
        if (content.getLength() == null) {
            content.setLength(0L);
        }
        return contentRepository.save(content);
    }

    @Transactional
    public Content createContent(MultipartFile file, String userId) throws UnsupportedContentType, NoSpaceLeft {
        var fileMetadata = storageService.save(file, userId);
        var content = Content.builder()
                .title(fileMetadata.getFileName())
                .contentType(fileMetadata.getFileType())
                .url(fileMetadata.getUrl())
                .build();

        content =  createContent(content, userId);
        fileMetadata.setContent(content);
        storageService.save(fileMetadata);
        return content;
    }

    @Transactional
    public Content updateContent(Long id, Content newContent, String userId) throws UnsupportedContentType {
        Content existingContent = contentRepository.findByIdAndUserId(id, userId).orElseThrow();
        try {
            storageService.findByContentId(id);
            if (!existingContent.getUrl().toString().equals(newContent.getUrl().toString()))
                storageService.deleteByContentId(id);
        } catch (NoSuchElementException ignored) {
        }

        existingContent.setTitle(newContent.getTitle());
        existingContent.setUrl(newContent.getUrl());
        existingContent.setContentType(newContent.getContentType());
        existingContent.setLength(newContent.getLength());
        existingContent.setPubDate(newContent.getPubDate());
        existingContent.setDescription(newContent.getDescription());
        existingContent.setTags(newContent.getTags());

        return contentRepository.save(existingContent);
    }

    public Content getContentById(Long id, String userId) {
        return contentRepository.findByIdAndUserId(id, userId).orElseThrow();
    }

    @Transactional
    public List<Content> getAllContent(String userId) {
        return contentRepository.findAllByUserId(userId);
    }

    @Transactional
    public long deleteContentById(Long id, String userId) {
        try {
            storageService.deleteByContentId(id);
        } catch (NoSuchElementException ignored) {
        }

        return contentRepository.deleteByIdAndUserId(id, userId);
    }

    @Transactional
    public Set<String> getAllTags(String userId) {
        return new HashSet<>(contentRepository.findDistinctTagsByUserId(userId));
    }
}
