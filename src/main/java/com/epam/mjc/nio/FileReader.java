package com.epam.mjc.nio;

import com.epam.mjc.nio.exceptions.InformationNotFoundException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {

    private String getAllLinesFromFile(File file) throws IOException {
        StringBuilder s = new StringBuilder();
        Path path = Paths.get(file.getAbsolutePath());

        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                s.append(line);
                s.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return s.toString();
    }

    private String getInfoByKey(String key, String text) throws InformationNotFoundException {
        int keyIdx = text.indexOf(key) + key.length();
        if (keyIdx < 0) {
            throw new InformationNotFoundException("Key [" + key + "] not found.");
        }

        int tailIdx = text.indexOf(System.lineSeparator(), keyIdx);
        if (tailIdx < keyIdx) {
            throw new InformationNotFoundException("key-value not found.");
        }

        return text.substring(keyIdx, tailIdx);
    }

    public Profile getDataFromFile(File file) {
        Profile profile = null;

        try {
            String s = getAllLinesFromFile(file);
            profile = new Profile(
                getInfoByKey("Name: ", s),
                Integer.valueOf(getInfoByKey("Age: ", s)),
                getInfoByKey("Email: ", s),
                Long.valueOf(getInfoByKey("Phone: ", s))
            );
        } catch (IOException | InformationNotFoundException e) {
            e.printStackTrace();
        }

        return profile;
    }
}
