package com.audiophileproject.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URL;

@FeignClient(name = "scraping-service")
public interface ScrapingProxy {
    @GetMapping("api/v1/scraper")
    String scrapUrl(@RequestParam("url") String url);
}
