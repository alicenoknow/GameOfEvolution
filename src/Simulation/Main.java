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
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public static void main(String[] args) {
        launch(args);
    }


    // Start application with displaying menu
    @Override
    public void start(Stage primaryStage) {
            String menuPath = "menu.fxml";
            String iconPath = "icon.png";
        try {
            URL filePath = this.getClass().getResource(menuPath);
            FXMLLoader fxmlLoader = new FXMLLoader(filePath);
            Parent root = fxmlLoader.load();

            primaryStage.setTitle("Menu");
            primaryStage.setScene(new Scene(root, 270, 500));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException | NullPointerException | IllegalStateException e) {
            log.log(Level.SEVERE, e.getMessage() + " Files necessary to run do not exist! " + menuPath);
            System.exit(0);
        }
        try{
            primaryStage.getIcons().add(new Image(iconPath));
        }
        catch (NullPointerException | IllegalStateException e){
           log.log(Level.WARNING, e.getMessage() + " Icon image not found! " + iconPath);
        }
    }
}
