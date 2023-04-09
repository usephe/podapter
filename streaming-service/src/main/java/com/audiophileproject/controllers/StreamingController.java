package com.audiophileproject.controllers;



import com.audiophileproject.services.StreamingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stream")
@RequiredArgsConstructor
@Slf4j
public class StreamingController {

    private final StreamingService streamingService;


    @GetMapping
    public void stream(HttpServletResponse response, @RequestHeader(value = HttpHeaders.RANGE, required = false) String range, @RequestHeader MultiValueMap<String, String> headers, @RequestParam("url") String url_str) {
        if(url_str.endsWith(".mp3")) {
            log.debug("URL "+url_str+" ENDS WITH .MP3");
            url_str = url_str.substring(0,url_str.length() - 4);
            log.debug("NEW URL "+url_str);
        }
        if (range == null) {
            log.debug("RANGE IS NULL");
        }
        try {
            streamingService.handleHTTPRangeRequest(response, streamingService.getTargetURL(url_str), headers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}