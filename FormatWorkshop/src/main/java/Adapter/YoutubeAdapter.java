package Adapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Converter;
import org.example.Log;
import org.example.YoutubeVideo;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.json.JSONTokener;
import org.jsoup.nodes.Document;
//
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class YoutubeAdapter implements VideoInformation{
    Converter ytVideo;
    Log logger = Log.createInstance();
    public YoutubeAdapter(Converter ytVideo) {
        this.ytVideo = ytVideo;
    }

    public void searchByName(String videoName){
        String keyword = videoName.replace(" ", "+");
        String key = System.getenv("ytKey");
        String url = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=" +keyword+"&key=" + key +"&maxResults=1";
        logger.info("Sending the following request: " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Received Youtube API response, deserializing required video id from json");
        ObjectMapper om = new ObjectMapper();
        Root root = null;
        try {
            root = om.readValue(response.body(), Root.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.ytVideo.passID(root.items.get(0).id.videoId);
        logger.info("The Video ID of the given Video name is " + root.items.get(0).id.videoId);
        logger.info("Sending Video ID to Downloader API");
    }

}
