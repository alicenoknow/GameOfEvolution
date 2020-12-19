package Simulation;

import Elements.Animal;
import Elements.Genotype;
import Elements.Herd;
import Map.WorldMap;
import Statistics.ComplexStatistics;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Utils.Config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulationEngine implements IEngine {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final WorldMap map;
    private final ArrayList<Animal> animals;
    private final ComplexStatistics statsCurrent;
    private WorldController controller;

    private int saveDay = -1;
    private int trackDay = -1;

    // Init simulation engine, init statistics, place first animals and init window for animation
    public SimulationEngine(WorldMap map) {
        this.map = map;
        this.animals = new ArrayList<>();
        this.statsCurrent = new ComplexStatistics();
        placeAdamAndEve();
        initStage();
    }

    private void placeAdamAndEve() {
        for (int i = 0; i < Config.getNumberOfAnimals(); i++) {
            Animal animal = new Animal(map);
            this.statsCurrent.updateAfterBirth(null, null, animal);
            if (map.place(animal))
                this.animals.add(animal);
        }
        this.statsCurrent.updateStatistics(animals);
    }

    // Window for animation and statistics
    private void initStage() {
        Stage stage = new Stage();
        String worldPath = "world.fxml";
        String iconPath = "icon.png";
        try {
            URL filePath = this.getClass().getResource(worldPath);
            FXMLLoader fxmlLoader = new FXMLLoader(filePath);
            Parent root = fxmlLoader.load();
            stage.setTitle("World");
            stage.setScene(new Scene(root, 760, 610));
            stage.setResizable(false);
            stage.show();

            this.controller = fxmlLoader.getController();
            controller.setSimulation(this, map, stage);
        } catch (IOException | NullPointerException | IllegalStateException e) {
            log.log(Level.SEVERE, e.getMessage() + " Files necessary to run do not exists! " + worldPath);
            System.exit(0);
        }
        try{
            stage.getIcons().add(new Image(iconPath));
        }
        catch(IllegalArgumentException | NullPointerException e){
            log.log(Level.WARNING, e.getMessage() + " Icon image not found! " + iconPath);
        }
    }

    // Check if saving or tracking operations should be performed on this day, then run simulation
    public void update() {
        if (saveDay == this.statsCurrent.getDay()) {
            this.statsCurrent.saveStats();
            this.saveDay = -1;
        }
        if (trackDay == this.statsCurrent.getDay()) {
            this.controller.displayTrackingStats(statsCurrent.getTrackingStats());
            this.trackDay = -1;
        }

        run();
        this.statsCurrent.updateStatistics(animals);
    }

    public void saveStatsAfter(int epochs) {
        this.saveDay = this.statsCurrent.getDay() + epochs;
    }

    public String getStatsToPrint() {
        return this.statsCurrent.getCurrentStatsToPrint();
    }

    // Set track day, start tracking new animal and its offspring
    public void trackAnimal(Animal animal, int epochs) {
        if (this.trackDay != -1) return;
        this.trackDay = this.statsCurrent.getDay() + epochs;
        this.statsCurrent.startTracking(animal, trackDay);
    }

    public Genotype getDominantGenotype() {
        return this.statsCurrent.getDominantGenotype();
    }

    // Cycle of the day
    @Override
    public void run() {
        // Animals die :c, dead animals are removed from map
        removeDead();

        // Every animal makes a move
        moveAnimals();

        // Get current herds, herds eat and procreate
        LinkedList<Herd> currentHerds = this.map.getHerds();
        eat(currentHerds);
        procreate(currentHerds);

        // Two grass are grown, first on the steppe, second in the jungle
        growGrasses();
    }

    private void eat(LinkedList<Herd> herds) {
        for (Herd herd : herds)
            if (herd.hasGrass()) {
                herd.splitGrass();
                this.map.removeGrassFromMap(herd.getPosition());
                this.statsCurrent.decreaseGrassNumber();
            }
    }

    private void removeDead() {
        for (Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal itAnimal = it.next();
            itAnimal.getOlder();

            if (itAnimal.getEnergy() <= 0) {
                itAnimal.dies();
                this.statsCurrent.updateAfterDeath(itAnimal);
                it.remove();
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : this.animals) {
            animal.move(this.map);
        }
    }

    private void procreate(LinkedList<Herd> herds) {
        for (Iterator<Herd> it = herds.iterator(); it.hasNext(); ) {
            Herd candidates = it.next();
            List<Animal> parents = candidates.pickParentsToBe();

            if (parents != null) {
                Animal child = new Animal(parents.get(0), parents.get(1), this.map.findChildPosition(parents.get(0).getPosition()));
                this.map.place(child);
                this.animals.add(child);
                this.statsCurrent.updateAfterBirth(parents.get(0), parents.get(1), child);
            }
        }
    }

    private void growGrasses() {
        int newGrasses = this.map.growGrasses();
        this.statsCurrent.increaseGrasses(newGrasses);
    }

}
