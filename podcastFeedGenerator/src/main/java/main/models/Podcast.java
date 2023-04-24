package main.models;

import com.rometools.rome.feed.synd.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Podcast {
    @Builder.Default
    private String title = "podapter";
    @Builder.Default
    private String description = "podapter";
    @Builder.Default
    private String owner = "podapter";
    @Builder.Default
    private String author = "podapter";
    private String category;
    private String language;
    private List<SyndEntry> entries;

    public SyndFeed generatePodcastFeed(int entriesLimit) {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(this.title);
        feed.setDescription(this.description);
        feed.setLanguage(this.language);
        feed.setLink("podapter.com");

        SyndImage image = new SyndImageImpl();
        image.setUrl("https://source.unsplash.com/random/400x400");
        image.setTitle("podifycast");
        image.setHeight(400);
        image.setWidth(400);
        feed.setImage(image);


        feed.setEntries(entriesLimit < 0 ? entries : entries.subList(0, Math.min(entries.size(), entriesLimit)));

        return feed;
    };
}
