package com.example.android.earcandy.models;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Mark Mamdouh on 8/27/2017.
 */

public class ReaderAndWriter {

    public void writeToFile(String data, String name, String path) {
        try {
            File root = new File(Environment.getExternalStorageDirectory() + path);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, name);
            if(!gpxfile.exists()){
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }else{
                gpxfile.delete();
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(data);
                writer.flush();
                writer.close();
            }
        } catch (IOException ignored) {}
    }

    public String readFromFile(String name, String path) {

        //Get the text file
        File file = new File(Environment.getExternalStorageDirectory() + path, name);

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException ignored) {}
        return String.valueOf(text);
    }

    public void deleteFile(String name, String path){
        File file = new File(Environment.getExternalStorageDirectory() + path + name.substring(4,name.length()));
        file.delete();
    }
}
