package Adapter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Thumbnails {
    @JsonProperty("default")
    public Default mydefault;
    public Medium medium;
    public High high;
}
