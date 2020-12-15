package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    // Start application with displaying menu
    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
            Parent root = fxmlLoader.load();

            primaryStage.setTitle("Menu");
            primaryStage.setScene(new Scene(root, 270, 500));
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("icon.png"));
            primaryStage.show();
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage() + " Files necessary to run do not exist!");
            System.exit(0);
        }
    }
}
