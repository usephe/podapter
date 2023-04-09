package com.audiophileproject.controlers;

import com.audiophileproject.services.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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