package Simulation;

import Elements.Animal;
import Elements.Grass;
import Elements.Herd;
import Elements.Vector2d;
import Map.WorldMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import Utils.Config;



public class WorldController {
    private Timeline timeline;
    private SimulationEngine simulation;
    private WorldMap map;
    private Animal clickedAnimal = null;
    private Circle clickedCircle = null;

    // Size of a single cell on map
    private final double cellWidth = 500.0 / Config.getWidth();
    private final double cellHeight = 500.0 / Config.getHeight();
    private final double radius = Math.min(cellWidth, cellHeight) / 4.0;

    @FXML
    private Label trackedStats;
    @FXML
    private Label stats;
    @FXML
    private Pane mapAnimation;
    @FXML
    private Label clickedGenotype;
    @FXML
    private TextField epochs;
    @FXML
    private CheckBox showDominantGenOwners;

    public void setSimulation(SimulationEngine simulation, WorldMap map, Stage stage) {
        this.simulation = simulation;
        this.map = map;
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Visualization does not support map with bigger length than 900
        // so if given map is bigger, display only statistics
        if (Math.max(Config.getWidth(), Config.getHeight()) < 900) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
                    actionEvent -> {                    // this will happen when timeline is running
                        simulation.update();
                        drawMap();
                        displayStatistics();
                    }));
            // Initial
            drawMap();
        } else
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(850),
                    actionEvent -> {
                        simulation.update();
                        displayStatistics();
                        // Just to look pretty when there is no animation
                        String imageName = "icon.png";
                        try {
                            Image image = new Image(imageName);
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(500);
                            imageView.setFitWidth(500);
                            mapAnimation.getChildren().add(imageView);
                        }
                        catch(IllegalArgumentException | NullPointerException e){
                            System.out.println(e.getMessage() + " Image not found! " + imageName);
                        }
                    }));
        stage.setOnCloseRequest(event -> timeline.stop());
        // Initial
        displayStatistics();

    }

    @FXML
    private void handlePlayButtonAction() {
        timeline.play();
    }

    @FXML
    private void handlePauseButtonAction() {
        timeline.stop();
    }

    @FXML
    private void handleSaveButtonAction() {
        int epochs = getEpochs();
        if (epochs != -1) {
            this.simulation.saveStatsAfter(epochs);
        }
    }

    @FXML
    private void handleFollowButtonAction() {
        int epochs = getEpochs();
        if (epochs != -1 && clickedAnimal != null) {
            this.simulation.trackAnimal(clickedAnimal, epochs);
        }
    }

    @FXML
    private int getEpochs() {
        try {
            String input = epochs.getText();
            if (input != null) {
                int epochs = Integer.parseInt(input);
                if (epochs > -1) return epochs;
            }
            return -1;
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
            return -1;
        }

    }

    private void displayStatistics() {
        stats.setText(simulation.getStatsToPrint());
    }

    public void displayTrackingStats(String stats) {
        trackedStats.setText(stats);
    }

    // Remove previous animation, draw new
    private void drawMap() {
        mapAnimation.getChildren().clear();
        drawSteppe();
        drawJungle();
        drawObjects();
    }

    private void drawSteppe() {
        mapAnimation.setBackground(new Background(new BackgroundFill(Color.rgb(255, 225, 120),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void drawJungle() {
        Rectangle jungle = new Rectangle();

        jungle.setTranslateX(cellWidth * Config.getJungleStartX());
        jungle.setTranslateY(cellHeight * Config.getJungleStartY());
        jungle.setWidth(cellWidth * Config.getJungleWidth());
        jungle.setHeight(cellHeight * Config.getJungleHeight());
        jungle.setFill(Color.rgb(154, 205, 50));

        mapAnimation.getChildren().add(jungle);
    }

    // Iterate over map and find objects to draw
    public void drawObjects() {
        for (int i = Config.getHeight(); i >= 0; i--)
            for (int j = 0; j <= Config.getWidth(); j++) {
                Vector2d currentPosition = new Vector2d(j, i);

                if (this.map.isOccupied(currentPosition)) {
                    Object object = this.map.objectAt(currentPosition);

                    if (object instanceof Herd) {
                        drawAnimal(getAnimalToDraw((Herd) object));
                    }
                    else drawGrass((Grass) object);
                }
            }
    }

    // Priority have animals with dominant genotype (when option of showing them is selected)
    // after them priority have tracked animal ( main > child > grandchild )
    // after them priority have strongest animal from herd
    private Animal getAnimalToDraw(Herd herd){
        Animal animal;
        if(showDominantGenOwners.isSelected())
            animal = herd.getAnimalWithDominantGenToDraw(this.simulation.getDominantGenotype());
        else{
            animal = herd.getAnimalToDraw();
        }
        return animal;
    }

    // Grass visualization
    private void drawGrass(Grass grass) {
        Circle grassCircle = new Circle(radius);
        grassCircle.setFill(grass.getColor());

        grassCircle.setTranslateX(cellWidth * grass.getPosition().x + 0.5 * cellWidth);
        grassCircle.setTranslateY(cellHeight * grass.getPosition().y + 0.5 * cellHeight);

        mapAnimation.getChildren().add(grassCircle);
    }

    // Animal visualization
    private void drawAnimal(Animal animal) {
        Circle animalCircle = new Circle(radius);

        if (showDominantGenOwners.isSelected())
            animalCircle.setFill(animal.getColor(this.simulation.getDominantGenotype()));
        else
            animalCircle.setFill(animal.getColor());


        animalCircle.setTranslateX(cellWidth * animal.getPosition().x + 0.5 * cellWidth);
        animalCircle.setTranslateY(cellHeight * animal.getPosition().y + 0.5 * cellHeight);

        // Procedure when animal is clicked
        EventHandler<MouseEvent> eventHandler = e -> {
            if (clickedCircle != null) clickedCircle.setFill(clickedAnimal.getColor());
            clickedCircle = animalCircle;
            clickedAnimal = animal;
            clickedCircle.setFill(Color.rgb(255, 130, 255));
            clickedGenotype.setText("Clicked animal genotype:\n" + animal.getGenotype().toString());
        };

        animalCircle.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        mapAnimation.getChildren().add(animalCircle);
    }
}
