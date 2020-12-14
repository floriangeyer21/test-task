package org.javafx.test;

import javafx.application.Application;
import org.javafx.test.configuration.JavaFXApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }

}
