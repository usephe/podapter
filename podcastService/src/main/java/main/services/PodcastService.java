package main.services;

import com.rometools.rome.feed.synd.*;
import lombok.RequiredArgsConstructor;
import main.dto.ContentDTO;
import main.models.Podcast;
import main.proxies.ContentProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PodcastService {
    private final ContentProxy contentProxy;
    public SyndFeed generatePodcastFeed(String userId, String tag, Date dateStart, Date dateEnd, int limit) {
        var contents = contentProxy.getAllContent(userId);
        List<ContentDTO> filteredContents = contents;
        if (dateStart != null || dateEnd != null)
            filteredContents = filteredContents.stream()
                    .filter(
                            content ->
                                    (dateStart == null || content.pubDate().after(dateStart)) &&
                                            (dateEnd == null || content.pubDate().before(dateEnd))
                    )
                    .collect(Collectors.toList());

        if (tag != null)
            filteredContents = filteredContents.stream()
                    .filter(
                            content -> content.tags().contains(tag)
                    )
                    .collect(Collectors.toList());

        filteredContents = limit < 0 ? filteredContents : filteredContents.subList(0, Math.min(contents.size(), limit));

        List<SyndEntry> entries = filteredContents.stream().map(
                content -> {
                    var entry = new SyndEntryImpl();
                    entry.setTitle(content.title());
                    // TODO: use the actual episode url as a GUID
                    entry.setUri(String.valueOf(content.id()));
                    entry.setPublishedDate(content.pubDate());

                    SyndContent description = new SyndContentImpl();
                    description.setValue(content.description());
                    entry.setDescription(description);

                    SyndEnclosure enclosure = new SyndEnclosureImpl();
                    enclosure.setUrl(content.url());
                    enclosure.setType(content.type());
                    enclosure.setLength(498537);
                    entry.setEnclosures(List.of(enclosure));
                    return entry;
                }
        ).collect(Collectors.toList());

        Podcast podcast = Podcast
                .builder()
                .entries(entries)
                .build();

        return podcast.generatePodcastFeed();
    }
}
