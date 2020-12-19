package Simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    // Start application with displaying menu
    @Override
    public void start(Stage primaryStage) {
        String menuPath = "menu.fxml";
        String iconPath = "icon.png";
        StageInitializer stageInit = new StageInitializer(primaryStage);
        stageInit.loadFXML(menuPath);
        stageInit.setStageView("Menu", 500, 270);
        stageInit.setIcon(iconPath);
    }
}
