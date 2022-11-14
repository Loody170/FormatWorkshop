package org.example;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;
import org.apache.commons.io.FilenameUtils;


import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

public class ImageConverter extends Converter{

    private String[] formats = {"bmp", "fax", "gif", "ico", "jpg", "jpeg", "mdi", "png", "psd", "tif", "tiff", "webp", "pdf"};

    private String srcPath;
    private String ext;
    private String destPath;
    private String dextExt;
    private JFileChooser fc = new JFileChooser();
    Log logger = Log.createInstance();

    public String getSource(JFileChooser fc){
        logger.info("Getting source file from user");
        System.out.println("*** Please select the image you want to convert from: ***");
        fc.setDialogTitle("Select the source file");
        fc.showOpenDialog(Main.p);
        File file = fc.getSelectedFile();
        srcPath = file.getAbsolutePath();
        logger.info("Source file path is " + srcPath);
        return srcPath;
    }

    public String getDestination(JFileChooser fc){
        logger.info("Getting destination folder from user");
        System.out.println("*** Please specify where to save the new image ***");
        fc.setDialogTitle("Select the folder destination");
        fc.setFileSelectionMode(DIRECTORIES_ONLY);
        fc.showOpenDialog(Main.p);
        destPath = fc.getSelectedFile().getAbsolutePath();
        logger.info("Destination folder path is " + destPath);
        return destPath;
    }
    @Override
    public void init(){
        srcPath = getSource(fc);
        destPath = getDestination(fc);
        ext = FilenameUtils.getExtension(srcPath);
        logger.info("Source file extension is " + ext);
    }

    @Override
    public void getOptions() {
        System.out.println("*** YOU CAN CONVERT IMAGES FRON AND TO THE FOLLOWING TYPES: ***");
        for (int i = 0; i < formats.length; i++) {
            System.out.print(formats[i] + ", ");
        }
        System.out.println("");
    }

    @Override
    public boolean checkConversion(String destExt) {
        logger.info("Format typed in by user is " + destExt);
        String url = "https://v2.convertapi.com/info/canconvert/" +ext+ "/to/" + destExt;
        logger.info("Sending the following request to check conversion compatability: " + url);

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
        String res = response.body();
        if (res.contains("Supported")){
            this.dextExt = destExt;
            logger.info("The formats are compatiable, starting conversion...");
            return true;
        }
        logger.error("The format the user choose is incorrect or not compatiable with the source file");
        return false;
    }

    @Override
    public void convert() {
          String key = System.getenv("conKey");
          Config.setDefaultSecret(key);
       // System.out.println("----");
       // System.out.println(ext);
       // System.out.println(dextExt);
       // System.out.println(srcPath);
       // System.out.println(destPath);
        logger.info("Sending conversion request...");
        try {
            ConvertApi.convert(ext, dextExt,
                    new Param("file", Paths.get(srcPath))
            ).get().saveFile(Paths.get(destPath)).get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Conversion is successful, saving file in destination folder.");
        System.out.println("Conversion is successful, saving file in destination folder.");
    }

    @Override
    public void passID(String id) {

    }

}
