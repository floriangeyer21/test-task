package org.javafx.test.controllers;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.NoArgsConstructor;
import org.javafx.test.service.FileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class Controller {
    @FXML
    private Button mainButton;
    @FXML
    private Label mainTitle;
    private HostServices hostServices;
    private String mainDirectory;
    private FileSearchService fileSearchService;

    @Autowired
    public Controller(HostServices hostServices,
                      @Value("${application.test.path}") String mainDirectory,
                      FileSearchService fileSearchService) {
        this.hostServices = hostServices;
        this.mainDirectory = mainDirectory;
        this.fileSearchService = fileSearchService;
    }

    @FXML
    public void initialize() {
        List<File> files = fileSearchService.displayFileTree(mainDirectory);
        mainButton.setOnAction(actionEvent -> {
            this.mainTitle.setText(files.stream().map(file -> file.getName() + "\n").collect(Collectors.joining()));
        });
    }


}
