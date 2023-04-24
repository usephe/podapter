package main.services;

import com.rometools.rome.feed.synd.*;
import lombok.RequiredArgsConstructor;
import main.proxies.YoutubeProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    @Value("${config.streaming.urlPrefix}")
    private String streamingUrlPrefix;
    private final YoutubeProxy youtubeProxy;

    public List<SyndEntry> youtubeEntriesMapper(List<SyndEntry> entries) {
        return entries.stream().map(
                entry -> {
                    SyndEntry syndEntry = new SyndEntryImpl();
                    syndEntry.setTitle(entry.getTitle());

                    syndEntry.setUri(String.valueOf(entry.getUri()));
                    syndEntry.setPublishedDate(entry.getPublishedDate());

                    SyndContent description = new SyndContentImpl();
                    SyndEnclosure enclosure = new SyndEnclosureImpl();
                    enclosure.setLength(0);
                    entry.getForeignMarkup().stream()
                            .filter(aForeignMarkup -> aForeignMarkup.getName().equals("group"))
                            .forEach(
                                    aForeignMarkup -> {
                                        description.setValue(aForeignMarkup.getChildText("description", aForeignMarkup.getNamespace()));
                                        // TODO: encode url before using it as value to a parameter
                                        enclosure.setUrl(streamingUrlPrefix + aForeignMarkup.getChild("content", aForeignMarkup.getNamespace()).getAttributeValue("url"));
                                        enclosure.setType("audio/webm");
                                    }
                            );

                    syndEntry.setDescription(description);

                    syndEntry.setEnclosures(List.of(enclosure));

                    return syndEntry;
                }
        ).toList();
    }

    public String getRssFeed(URL url) {
        if (isYoutubePlaylistURL(url))
            return youtubeProxy.getPlaylistRssFeed(getPlaylistId(url).orElseThrow(() -> new IllegalArgumentException("Invalid playlist id")));
        if (isYoutubeChannelUrl(url))
            return youtubeProxy.getChannelRssFeed(getChannelId(url).orElseThrow(() -> new IllegalArgumentException("Invalid channel id")));

        throw new IllegalArgumentException("Invalid url: " + url);
    }

    public Optional<String> getChannelId(URL url) {
        Pattern pattern = Pattern.compile("^/channel/(UC[-_a-zA-Z0-9]{22})$");
        Matcher matcher = pattern.matcher(url.getPath());
        return Optional.ofNullable(matcher.find() ? matcher.group(1) : null);
    }

    public Optional<String> getPlaylistId(URL url) {
        return Optional.ofNullable(getParamsMap(url.getQuery()).get("list"));
    }

    public boolean isYoutubePlaylistURL(URL url) {
        return url.getPath().startsWith("/playlist");
    }

    public boolean isYoutubeChannelUrl(URL url) {
        return url.getPath().startsWith("/channel");
    }

    private static Map<String, String> getParamsMap(String query) {
        Map<String, String> params = new HashMap<>();

        if (query == null) {
            return params;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8) : null;
            params.put(key, value);
        }

        return params;
    }
}
