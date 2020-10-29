package com.fuzzyrock.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
    private FileUtils() {

    }

    public static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line + '\n');
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
