package com.audiophileproject.main.dto;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.services.ExtractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class ContentDTOMapper implements Function<Content, ContentDTO> {
    @Value("${config.streaming.urlPrefix}")
    private String streamingUrlPrefix;
    private final ExtractorService extractorService;

    @Override
    public ContentDTO apply(Content content) {
        URL contentUrl = content.getUrl();
        if (extractorService.isSupportedSite(content.getUrl())) {
            try {
                contentUrl = new URL(streamingUrlPrefix + URLEncoder.encode(content.getUrl().toString(), StandardCharsets.UTF_8));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .url(contentUrl)
                .contentType(content.getContentType())
                .length(content.getLength() != null ? content.getLength() : 0)
                .pubDate(content.getPubDate())
                .description(content.getDescription())
                .tags(content.getTags())
                .build();
    }
}
