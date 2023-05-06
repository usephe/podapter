package com.audiophileproject.controlers;

import com.audiophileproject.services.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/scraper")
public class ScrapingController {
    private final ScrapingService scrapingService;

    @GetMapping
    public String scrapUrl(@RequestParam(name = "url") URL url){
        return scrapingService.scrapUrl(url);
    }
}