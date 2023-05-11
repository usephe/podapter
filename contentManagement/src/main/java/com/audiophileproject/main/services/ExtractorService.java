package com.audiophileproject.main.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ExtractorService {
    private static final Pattern YOUTUBE_PATTERN = Pattern.compile("https?://(?:m\\.)?(?:www\\.)?youtu(?:\\.be/|(?:be-nocookie|be)\\.com/(?:watch|[\\w]+\\?(?:feature=[\\w]+\\.[\\w]+&)?v=|v/|e/|embed/|live/|shorts/|user/(?:[\\w#]+/)+))([^&#?\\n]+)");

    public boolean isSupportedSite(String url) {
        Matcher matcher = YOUTUBE_PATTERN.matcher(url);
        return matcher.matches();
    }

    public boolean isSupportedSite(URL url) {
        return isSupportedSite(url.toString());
    }
}
