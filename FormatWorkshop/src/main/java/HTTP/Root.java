package HTTP;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root{
    public String guid;
    public String message;
    @JsonProperty("YoutubeAPI")
    public YoutubeAPI youtubeAPI;
}