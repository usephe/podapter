package main.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "youtube", url = "https://www.youtube.com/feeds/videos.xml")
public interface YoutubeProxy {
    @GetMapping
    String getPlaylistRssFeed(@RequestParam("playlist_id") String playlistId);
    @GetMapping
    String getChannelRssFeed(@RequestParam("channel_id") String channelId);
}
