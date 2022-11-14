package org.example;

public class ConverterFactory {
    public Converter getConverter(String type){
        if(type == null) {
            return null;

        } if(type.equalsIgnoreCase("Document") || type.equalsIgnoreCase("doc")){
            return new DocumentConverter();
        } else if(type.equalsIgnoreCase("Image") || type.equalsIgnoreCase("img")){
            return new ImageConverter();
        }
        else if(type.equalsIgnoreCase("Youtube Video") || type.equalsIgnoreCase("youtube")){
            return new YoutubeVideo();
        }
        return null;
    }
}
