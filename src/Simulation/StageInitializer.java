package Simulation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StageInitializer {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final Stage stage;
    private FXMLLoader fxmlLoader;
    private Parent root;

    public StageInitializer(Stage stage){
        this.stage = stage;
    }

    public StageInitializer(){
        this(new Stage());
    }

    public void loadFXML(String path){
        try {
            URL filePath = this.getClass().getResource(path);
            this.fxmlLoader = new FXMLLoader(filePath);
            this.root = fxmlLoader.load();
        }
        catch(IOException | NullPointerException | IllegalStateException e){
            log.log(Level.SEVERE, "Cannot load fxml file! " + e.getMessage());
            System.exit(0);
        }
    }

    public void setStageView(String title, int height, int width){
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.setResizable(false);
        stage.show();
    }

    public Object getController(){
        return fxmlLoader.getController();
    }

    public void setIcon(String path) {
        try {
            stage.getIcons().add(new Image(path));
        }
        catch(NullPointerException | IllegalStateException e){
            log.log(Level.WARNING, "Cannot load icon! " + e.getMessage());
        }
    }

    public Stage getStage(){
        return this.stage;
    }
}
