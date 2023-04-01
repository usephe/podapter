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
                .type(content.getType())
                .length(content.getLength())
                .pubDate(content.getPubDate())
                .description(content.getDescription())
                .tags(content.getTags())
                .build();
    }
}
