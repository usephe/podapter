package com.audiophileproject.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamingService {

    private final RestTemplate restTemplate;

    @Cacheable(value = "urlCache", cacheManager = "springCM")
    public String getTargetURL(String url_str) {

        Date start = new Date();
        log.debug("GETTING ACTUAL URL...");

        // set the URL endpoint and the parameter value
        String scrap_url = "http://localhost:8080/api/v1/scraper?url={urlValue}";
        String urlValue = url_str;

        // create a URI object with the endpoint and the parameter value
        URI uri = UriComponentsBuilder.fromUriString(scrap_url)
                .buildAndExpand(urlValue)
                .toUri();

        // fetch the target url
        String targetURLString = restTemplate.getForObject(
                uri,
                String.class
        );
        if(targetURLString == null) throw new RuntimeException("Something went wrong: Can't get the audio file");

        log.debug("target url --->");
        log.debug(targetURLString);
        Date end = new Date();
        log.debug("DONE SCRAPING IN "+ (end.getTime() - start.getTime())+" ms");
        return targetURLString;
    }


    public void handleHTTPRangeRequest(HttpServletResponse response, String targetURLString, MultiValueMap<String,String> headers) {

        String targetHost;
        URL targetURL;
        try{
            targetURL = new URL(targetURLString);
            targetHost = targetURL.getHost();
        }catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
        log.debug("TARGET HOST -----> "+targetHost);
        // correct Host header
        headers.set(HttpHeaders.HOST,targetHost);
        log.debug("HEADERS FROM CLIENT ---> "+headers.toString());
        // remove contenct header
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        URLConnection connection = null;
        try{
            log.debug("OPENNING NEW CONNECTION");
            connection = targetURL.openConnection();
        }catch (IOException e){
            System.out.println("IO EXCEPTION: COULDN'T OPEN CONNECTION TO "+targetHost);
            log.debug(e.getMessage());
        }

        if(headers.containsKey("range")){
            log.debug("IS RANGE REQUEST");
            String range = headers.get("range").get(0);
            log.debug("IS RANGE CORRECT? "+range);
            connection.setRequestProperty("Range",range);
            response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
        }else log.debug("NOT A RANGE REQUEST ");

        InputStream inputStream = null;

        try{
             inputStream = connection.getInputStream();
        }catch (IOException e){
            System.out.println("INPUT STREAM CLOSED");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Map<String, List<String>> responseHeadersMap = connection.getHeaderFields();

        log.debug("RESPONSE HEADERS MAP "+responseHeadersMap);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeadersMap.forEach((headerName,values) -> {
            if(!(headerName==null)){
                log.debug("KEY --> "+headerName+" VALUE--> "+values.get(0));
                responseHeaders.set(headerName,values.get(0));
                response.setHeader(headerName,values.get(0));
            }
        });

//        // adding content disposition - hopefully, this will let the podcast client show the episodes
//        if(response.getHeader("Content-Range") != null){
//            System.out.println("INJECTING CONTENT DISPOSITION");
//            String range = responseHeaders.get("Content-Range").get(0).split(" ")[1];
//            response.setHeader("Content-Disposition",ContentDisposition.attachment().filename("chunk-"+range+".mp3").build().toString());
//            responseHeaders.setContentDisposition(ContentDisposition.attachment().filename("chunk-"+range+".mp3").build());
//            System.out.println("CONTENT DISPOSITION ---> "+response.getHeader("Content-Disposition"));
//        }else{
//            System.out.println("NO CONTENT RANGE????");
//        }

        System.out.println("RESPONSE HEADERS "+responseHeaders);

        try{
            IOUtils.copy(inputStream,response.getOutputStream());
        }catch (IOException e){
            System.out.println("STREAM TO CLIENT CLOSED");
        }
    }
}
