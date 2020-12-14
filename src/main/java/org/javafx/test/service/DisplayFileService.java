package org.javafx.test.service;

import org.apache.commons.io.FilenameUtils;
import org.javafx.test.exceptions.FileProcessingException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DisplayFileService {

    public List<String> displayFileContent(File file) {
        List<String> result = new CopyOnWriteArrayList<>();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
            return result;
        } catch (IOException e) {
            throw new FileProcessingException("Can't read data from file " + file.getName(), e);
        }
    }
}
