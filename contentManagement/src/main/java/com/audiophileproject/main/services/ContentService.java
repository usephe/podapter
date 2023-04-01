package com.audiophileproject.main.services;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.repositories.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ContentService {
	private final ContentRepository contentRepository;

    // TODO: move the heavy processing after responding to client
    public Content createContent(Content content, String userId) {
        content.setUserId(userId);
        if (content.getPubDate() == null)
            content.setPubDate(new Date());
        if (content.getType() == null) {
            try {
                content.setType(getAudioType(content.getUrl()));
            } catch (IOException e) {
                System.out.println("Unsupported URL type");
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

    private String getAudioType(String url) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(new URL(url));
        return mimeType;
//        return mimeType.startsWith("audio/") ? mimeType.substring(6) : "Unknown";
    }
}
