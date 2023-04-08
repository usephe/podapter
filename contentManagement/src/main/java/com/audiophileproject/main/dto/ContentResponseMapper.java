package com.audiophileproject.main.dto;

import com.audiophileproject.main.models.Content;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ContentResponseMapper implements Function<Content, ContentResponse> {
    @Override
    public ContentResponse apply(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .url(content.getUrl())
                .contentType(content.getContentType())
                .length(content.getLength() != null ? content.getLength() : 0)
                .pubDate(content.getPubDate())
                .description(content.getDescription())
                .tags(content.getTags())
                .build();
    }
}
