package main.controllers;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import lombok.RequiredArgsConstructor;
import main.services.PodcastService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/podcast")
public class PodcastController {

    private final PodcastService podcastService;

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_RSS_XML_VALUE)
    public String getPodcastFeed(
            @PathVariable String userId,
            @RequestParam(defaultValue = "-1", required = false) int limit,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag
    ) throws FeedException {
        SyndFeed feed = podcastService.generatePodcastFeed(userId, title, tag, dateStart, dateEnd, limit);
        SyndFeedOutput xmlFeed = new SyndFeedOutput();
        return xmlFeed.outputString(feed);
    }

    @GetMapping(produces = MediaType.APPLICATION_RSS_XML_VALUE)
    public String getPodcastFeed(
            @RequestParam URL url,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "-1", required = false) int limit
    ) throws FeedException, IOException {
        SyndFeed feed = podcastService.generatePodcastFeed(url, title, limit);
        SyndFeedOutput xmlFeed = new SyndFeedOutput();
        return xmlFeed.outputString(feed);
    }
}
