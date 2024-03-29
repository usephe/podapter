package com.audiophileproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
@Service
public class ScrapingService {
    private final YoutubeDLService youtubeDLService;

    public String scrapUrl(URL url){
        // this will ensure that the URL is a full reference to a file
        if(url.getProtocol().isBlank() || url.getHost().isBlank() || url.getFile().isBlank())
        {
            throw new RuntimeException("Bad URL");
        }
        String url_string = url.getProtocol()+"://"+url.getHost()+url.getFile();
        String targetUrlString = "";
//        log.debug("SCRAPING AUDIO --------> "+url_string);
//        ProcessBuilder pb = new ProcessBuilder("yt-dlp","--get-url","--format","bestaudio",url_string);

//        // Executes the youtubedl process
//        try{
//            Process process = pb.start();
//            InputStream stdout = process.getInputStream();
//            InputStream stderr = process.getErrorStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stdout));
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(stderr));
//            targetUrlString = bufferedReader.readLine();
//            String errorString = errorReader.readLine();
//            if(!errorString.isBlank()) log.debug(" YOUTUBEDL ERROR: ----> "+targetUrlString);
//
//            log.debug("TARGET URL: ----> "+targetUrlString);
//        }catch (Exception e){
//            throw new RuntimeException("Something went wrong");
//        }

        try{
            targetUrlString = youtubeDLService.getTargetURL(url);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        try{
            // test the connection if the URL valid or not
            URL targetUrl = new URL(targetUrlString);
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setRequestMethod("HEAD");
            int responseCode = conn.getResponseCode();
            if(!(responseCode == HttpURLConnection.HTTP_OK)) throw new RuntimeException("Invalid URL");
        }catch (Exception e){
            throw new RuntimeException("URL Not Valid");
        }

        return targetUrlString;
    }
    public RedirectView scrapUrlAndRedirect(URL url){


        // this will ensure that the URL is a full reference to a file
        if(url.getProtocol().isBlank() || url.getHost().isBlank() || url.getFile().isBlank())
        {
            throw new RuntimeException("Bad URL");
        }
        String url_string = url.getProtocol()+"://"+url.getHost()+url.getFile();
        String targetUrlString = "";
//        log.debug("SCRAPING AUDIO --------> "+url_string);
//        ProcessBuilder pb = new ProcessBuilder("yt-dlp","--get-url","--format","bestaudio",url_string);

//        // Executes the youtubedl process
//        try{
//            Process process = pb.start();
//            InputStream stdout = process.getInputStream();
//            InputStream stderr = process.getErrorStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stdout));
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(stderr));
//            targetUrlString = bufferedReader.readLine();
//            String errorString = errorReader.readLine();
//            if(!errorString.isBlank()) log.debug(" YOUTUBEDL ERROR: ----> "+targetUrlString);
//
//            log.debug("TARGET URL: ----> "+targetUrlString);
//        }catch (Exception e){
//            throw new RuntimeException("Something went wrong");
//        }

        try{
            targetUrlString = youtubeDLService.getTargetURL(url);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        try{
            // test the connection if the URL valid or not
            URL targetUrl = new URL(targetUrlString);
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setRequestMethod("HEAD");
            int responseCode = conn.getResponseCode();
            if(!(responseCode == HttpURLConnection.HTTP_OK)) throw new RuntimeException("Invalid URL");
        }catch (Exception e){
            throw new RuntimeException("URL Not Valid");
        }

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(targetUrlString);

        return redirectView;
    }
}
