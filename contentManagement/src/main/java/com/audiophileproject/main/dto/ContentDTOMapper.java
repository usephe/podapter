package com.audiophileproject.main.dto;

import com.audiophileproject.main.models.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class ContentDTOMapper implements Function<Content, ContentDTO> {
    @Override
    public ContentDTO apply(Content content) {
        return ContentDTO.builder()
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
