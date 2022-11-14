package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static Log instance;
    DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
    String currentDateTimeString = LocalDateTime.now().format(myFormatter);
    private final String logFile = currentDateTimeString + ".txt";
    private PrintWriter writer;

    private Log() {
        try {
            FileWriter fw = new FileWriter(logFile);
            writer = new PrintWriter(fw, true);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Log createInstance(){
        if(instance == null){
            instance = new Log();
        }
        return instance;
    }
    public void info (String message) {
        writer.println("INFO: " + message);

    }
    public void error (String message) {
        writer.println("Error: " + message);

    }
}
