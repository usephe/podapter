package com.audiophileproject.services;

import com.audiophileproject.models.YoutubeDL;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Service
public class ScrapingService {

    public RedirectView scrapUrl(URL url){


        // this will ensure that the URL is a full reference to a file
        if(url.getProtocol().isBlank() || url.getHost().isBlank() || url.getFile().isBlank())
        {
            throw new RuntimeException("Bad URL");
        }
        String url_string = url.getProtocol()+"://"+url.getHost()+url.getFile();
        String targetUrlString = "";
//        System.out.println("SCRAPING AUDIO --------> "+url_string);
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
//            if(!errorString.isBlank()) System.out.println(" YOUTUBEDL ERROR: ----> "+targetUrlString);
//
//            System.out.println("TARGET URL: ----> "+targetUrlString);
//        }catch (Exception e){
//            throw new RuntimeException("Something went wrong");
//        }

        try{
            YoutubeDL ytdlp = new YoutubeDL();
            targetUrlString = ytdlp.getTargetURL(url);
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
