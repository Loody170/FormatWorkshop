package org.example;

import Adapter.VideoInformation;
import Adapter.YoutubeAdapter;
import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

public class Main {
    public static JPanel p = new JPanel();
    public static Scanner in = new Scanner(System.in);
    public static String answer;
    Log logger = Log.createInstance();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Log logger = Log.createInstance();

        logger.info("Initiating Program");

        System.out.println("*** Welcome to Format WorkShop, your best place for file conversion ***");
        while (true) {
            System.out.println("*** Please select the type of file you want to convert from [Document, Image, Youtube video] ***");
            answer = in.nextLine();

            ConverterFactory factory = new ConverterFactory();
            Converter converter = factory.getConverter(answer);
            logger.info("User picked and created a: " + answer + " converter");

            if (answer.equalsIgnoreCase("youtube video") || answer.equalsIgnoreCase("youtube"))
                createAdapter(converter, logger);

            converter.conversion();
            System.out.println("\nDo you want to make an another conversion? [y/n]");
            answer = in.nextLine();
            if(answer.equalsIgnoreCase("y")){
                logger.info("User choose to make another conversion");
            }

            else if(answer.equalsIgnoreCase("n")){
                logger.info("User choose to stop the program");
                System.out.println("Exiting program, thank you for using Format Workshop!");
                break;
            }
        }//loop brackets

    }//main

    public static void createAdapter(Converter converter, Log logger){
        VideoInformation ytAdapter = new YoutubeAdapter(converter);
        System.out.println("*** Please type in the name of the YouTube video ***");
        answer = in.nextLine();
        logger.info("Video name is: " + answer);
        ytAdapter.searchByName(answer);
    }

}