package org.javafx.test.configuration;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.NoArgsConstructor;
import org.javafx.test.Main;
import org.javafx.test.service.DisplayFileService;
import org.javafx.test.service.FileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class JavaFXApplication extends Application {
    private ConfigurableApplicationContext context;
    private FileSearchService fileSearchService;
    private DisplayFileService displayFileService;
    @Value("${application.test.path}")
    private String mainDirectory;

    @Override
    public void init() throws Exception {
        this.fileSearchService = new FileSearchService();
        this.displayFileService = new DisplayFileService();
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                ac -> {
                    ac.registerBean(Application.class, () -> JavaFXApplication.this);
                    ac.registerBean(Parameters.class, this::getParameters);
                    ac.registerBean(HostServices.class, this::getHostServices);
                };

        this.context = new SpringApplicationBuilder()
                .sources(Main.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*this.context.publishEvent(new StageReadyEvent(stage));*/
        Button btn = new Button();
        btn.setText("Display files tree");

        final StackPane root = new StackPane();
        AnchorPane editorRoot = new AnchorPane();
        editorRoot.getChildren().add(btn);
        root.getChildren().add(editorRoot);

        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add("/styles.css");

        primaryStage.setScene(scene);
        primaryStage.show();

        HBox fileRoot = new HBox();
        VBox menu = new VBox();
        menu.setStyle("-fx-background-color: blue;");
        menu.setFillWidth(true);
        fileRoot.getChildren().add(menu);
        List<File> files = fileSearchService.displayFileTree(mainDirectory);
        btn.setOnAction(actionEvent -> {
            for (int i = 0; i < files.size(); i++) {
                Button button = new Button(files.get(i).getName());
                if (files.get(i).isFile()) {
                    StringBuilder content = new StringBuilder();
                    for (String str : displayFileService.displayFileContent(files.get(i))) {
                        content.append(str).append("\n");
                    }
                    button.setOnAction(actionEvent1 -> {
                        Label label = new Label();
                        label.setText(content.toString());
                        editorRoot.getChildren().add(label);
                    });
                }
                button.setPrefWidth(100);
                button.getStyleClass().add("custom-menu-button");
                menu.getChildren().add(button);
            }
            root.getChildren().addAll(fileRoot);
        });
    }


    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}
