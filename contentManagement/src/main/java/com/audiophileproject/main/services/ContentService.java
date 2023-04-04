package com.audiophileproject.main.services;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.repositories.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    public Content createContent(Content content, String userId) {
        content.setUserId(userId);
        if (content.getPubDate() == null)
            content.setPubDate(new Date());
        if (content.getContentType() == null) {
            try {
                URL url = content.getUrl();
                URLConnection connection = url.openConnection();
                content.setLength(connection.getContentLengthLong());
                String contentType = connection.getContentType();
                content.setContentType(contentType);
            } catch (IOException e) {
                System.out.println("Unsupported URL contentType");
            }
        }

        return contentRepository.save(content);
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Content> getAllContent(String userId) {
        return contentRepository.findAllByUserId(userId);
    }

    public void deleteContentById(Long id) {
        var content = contentRepository.findById(id).orElse(null);
        if (content == null)
            return;

        contentRepository.deleteById(id);
    }
}
