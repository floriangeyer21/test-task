package org.javafx.test.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FileSearchService {

    public List<File> displayFileTree(String path) {
        File file = new File(FilenameUtils.separatorsToSystem(path));
        List<File> files = Arrays.asList(Objects.requireNonNull(file.listFiles()));
        return files;
    }
}
