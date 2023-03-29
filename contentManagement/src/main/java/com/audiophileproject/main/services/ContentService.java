package com.audiophileproject.main.services;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.repositories.ContentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContentService {
	private final ContentRepository contentRepository;

    public Content createContent(Content content, String userId) {
        content.setUserId(userId);
        return contentRepository.save(content);
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    public void deleteContentById(Long id) {
        var content = contentRepository.findById(id).orElse(null);
        if (content == null)
            return;

        contentRepository.deleteById(id);
    }
}
