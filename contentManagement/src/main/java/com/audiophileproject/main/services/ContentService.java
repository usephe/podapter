package com.audiophileproject.main.services;

import com.audiophileproject.main.exceptions.UnsupportedContentType;
import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.repositories.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ContentService {
	private final ContentRepository contentRepository;

    public Content createContent(Content content, String userId) throws UnsupportedContentType {
        content.setUserId(userId);
        if (content.getPubDate() == null)
            content.setPubDate(new Date());
        URL url = content.getUrl();

        URLConnection connection = null;
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
    public Content updateContent(Long id, Content newContent, String userId) throws UnsupportedContentType {
        Content existingContent = contentRepository.findByIdAndUserId(id, userId).orElseThrow();

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
    public void deleteContentById(Long id, String userId) {
        long numberOfEntriesDeleted = contentRepository.deleteByIdAndUserId(id, userId);
        if (numberOfEntriesDeleted == 0)
            throw new NoSuchElementException();
    }
}
