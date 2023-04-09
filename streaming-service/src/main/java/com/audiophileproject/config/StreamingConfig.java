package com.audiophileproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableCaching
@EnableDiscoveryClient
@Slf4j
public class StreamingConfig {

    @Bean(name = "springCM")
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager mgr = new ConcurrentMapCacheManager();
        mgr.setCacheNames(List.of("urlCache"));
        return mgr;
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder().additionalInterceptors((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    log.debug("Request headers: "+headers);
                    ClientHttpResponse response = execution.execute(request, body);
                    HttpHeaders responseHeaders = response.getHeaders();
                    log.debug("Response headers: "+ responseHeaders);
                    return response;
                })
                .build();
    }
}
