package main.controllers;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import lombok.RequiredArgsConstructor;
import main.services.PodcastService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
            @RequestParam(required = false) String tag
    ) throws FeedException {
        System.out.println("dateStart = " + dateStart);
        System.out.println("dateEnd = " + dateEnd);
        SyndFeed feed = podcastService.generatePodcastFeed(userId, tag, dateStart, dateEnd, limit);
        SyndFeedOutput xmlFeed = new SyndFeedOutput();
        return xmlFeed.outputString(feed);
    }
}