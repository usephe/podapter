package com.audiophileproject.models;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class YoutubeDL {
    @Value("${config.youtubedl.path}")
    private String path = "yt-dlp";
    private boolean isInstalled = false;

    public YoutubeDL() throws Exception{
        ProcessBuilder pb = new ProcessBuilder(this.path,"--version");
        Process process = pb.start();
        process.waitFor();
        if(process.exitValue() == 0) isInstalled = true;
        else throw new Exception("Youtubedl not found");
    }
    public YoutubeDL(String path) throws Exception{
        this.path = path;
        ProcessBuilder pb = new ProcessBuilder(path,"--version");
        Process process = pb.start();
        if(process.exitValue() == 0) isInstalled = true;
        else throw new Exception("Youtubedl not found");
    }

    /**
     * gets the actual URI to the file from the page url
     * @param url
     * @return
     */
    public String getTargetURL(URL url){
        if(!this.isInstalled) return null;

        String url_string = url.getProtocol()+"://"+url.getHost()+url.getFile();
        String targetUrlString = "";
        System.out.println("SCRAPING AUDIO --------> "+url_string);
        ProcessBuilder pb = new ProcessBuilder("yt-dlp","--get-url","--format","bestaudio",url_string);
        try{
            Process process = pb.start();
            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(stderr));
            targetUrlString = bufferedReader.readLine();
            String errorString = errorReader.readLine();
            if(!(errorString == null) && !errorString.isBlank()) System.out.println(" YOUTUBEDL ERROR: ----> "+targetUrlString);

            System.out.println("TARGET URL: ----> "+targetUrlString);
            return targetUrlString;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Something went wrong");
        }
    }
}
