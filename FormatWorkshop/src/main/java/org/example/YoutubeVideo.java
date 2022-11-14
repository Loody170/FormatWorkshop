package org.example;



import HTTP.Root;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class YoutubeVideo extends Converter{
    private String[] formats = {"mp3", "mp4"};
    String destExt;
    String videoID;
    Log logger = Log.createInstance();

    @Override
    public void passID(String id) {
        this.videoID = id;
    }
    @Override
    public void getOptions() {
        System.out.println("*** YOU CAN CONVERT YOUTUBE VIDEOS TO THE FOLLOWING TYPES: ***");
        for (int i = 0; i < formats.length; i++) {
            System.out.print(formats[i] + ", ");
        }
        System.out.println("");
    }
    @Override
    public void init() {
        logger.info("initiation for the YoutubeVideo class");
    }

    @Override
    public boolean checkConversion(String type) {
        logger.info("Format typed in by user is " + type);
        for (int i = 0; i < formats.length; i++) {
            if(formats[i].equalsIgnoreCase(type)) {
                this.destExt = type;
                logger.info("The formats are compatiable, starting conversion...");
                return true;
            }
        }
        logger.error("The format the user choose is incorrect or not compatiable with the source file");
        return false;
    }

    @Override
    public void convert() {
        String key = System.getenv("rapidKey");
        String url = "https://t-one-youtube-converter.p.rapidapi.com/api/v1/createProcess?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3D"+videoID+"&format="+destExt+"&responseFormat=json&regenerate=true";

        //String url = "https://t-one-youtube-converter.p.rapidapi.com/api/v1/createProcess?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3D"+videoID+"&format="+destExt+"&responseFormat=json&regenerate=true&lang=en";
        logger.info("Sending the following conversion request: " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", key)
                .header("X-RapidAPI-Host", "t-one-youtube-converter.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        logger.info("full request is:  " + request.toString());
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Received response from API, deserializing from json and passing required download link");

        ObjectMapper om = new ObjectMapper();
        Root root = null;
        while (true){
            try {
                root = om.readValue(response.body(), Root.class);
                if(destExt.equalsIgnoreCase("mp3")) {
                    logger.info("Preparing mp3 link and sending it to the browser");
                    logger.info(root.youtubeAPI.urlMp3);
                    Desktop.getDesktop().browse(new URL(root.youtubeAPI.urlMp3).toURI());
                    break;
                }//if
                else {
                    logger.info("Preparing mp4 link and sending it to the browser");
                    logger.info(root.youtubeAPI.urlVideo);
                    Desktop.getDesktop().browse(new URL(root.youtubeAPI.urlVideo).toURI());
                    break;
                }//else
            }//try block
            catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                logger.error("Malformed url exception, no video url provided from api. Handling exception.");
                System.err.println("Unfurtunately, we could not make a video download url for you.");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Would you like an audio download instead? [Yes, No]");
                String answer = Main.in.nextLine();
                if(answer.equalsIgnoreCase("Yes")){
                    destExt = "mp3";
                    continue;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }//method

}
