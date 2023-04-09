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
            if (!isSupportedMediaType(contentType))
                throw new UnsupportedContentType("(" + contentType + ") Unsupported content type");
            content.setContentType(contentType);
        }
        if (content.getLength() == null) {
            content.setLength(0L);
        }
        return contentRepository.save(content);
    }

    @Transactional
    public Content updateContent(Long id, Content newContent, String userId) {
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
    private boolean isSupportedMediaType(String type) {
        MediaType mediaType = MediaType.parseMediaType(type);
        return isSupportedAudioType(mediaType);
    }
    private boolean isSupportedAudioType(MediaType mediaType) {
        return mediaType.getType().equals("audio");
    }

    public Content getContentById(Long id, String userId) {
        return contentRepository.findByIdAndUserId(id, userId).orElseThrow();
    }

    @Transactional
    public List<Content> getAllContent(String userId) {
        return contentRepository.findAllByUserId(userId);
    }

    public void deleteContentById(Long id, String userId) {
        contentRepository.delete(contentRepository.findByIdAndUserId(id, userId).orElseThrow());
    }
}
