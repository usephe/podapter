package com.audiophileproject.main.dto;

import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.services.ExtractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class ContentMapper implements Function<ContentDTO, Content> {
    @Value("${config.streaming.urlPrefix}")
    private String streamingUrlPrefix;
    private final ExtractorService extractorService;

    @Override
    public Content apply(ContentDTO contentDTO) {
        String contentURL = contentDTO.url().toString();
        if (contentURL.startsWith(streamingUrlPrefix))
            contentURL = URLDecoder.decode(contentURL.substring(streamingUrlPrefix.length()), StandardCharsets.UTF_8);

        try {
            return Content.builder()
                    .title(contentDTO.title())
                    .url(new URL(contentURL))
                    .contentType(contentDTO.contentType())
                    .length(contentDTO.length())
                    .pubDate(contentDTO.pubDate())
                    .description(contentDTO.description())
                    .tags(contentDTO.tags())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
