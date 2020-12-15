package Statistics;

import Elements.Animal;
import Elements.Genotype;
import Utils.TrackStatus;
import Utils.Tracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ComplexStatistics extends Statistics {
    private final Statistics generalStats;
    private Tracker tracker;

    // Get set of own current statistics and set of general statistics
    public ComplexStatistics() {
        super();
        this.generalStats = new Statistics();
    }

    // When new animal added to simulation, check if it should be track, update statistics and tracker
    public void updateAfterBirth(Animal parent1, Animal parent2, Animal newAnimal) {
        if (parent1 != null && parent2 != null)
            if (parent1.checkTrackStatus() == TrackStatus.MAIN || parent2.checkTrackStatus() == TrackStatus.MAIN)
                tracker.addChild(newAnimal);
            else if (parent1.checkTrackStatus() == TrackStatus.CHILD ||
                    parent2.checkTrackStatus() == TrackStatus.CHILD ||
                    parent1.checkTrackStatus() == TrackStatus.GRANDCHILD ||
                    parent2.checkTrackStatus() == TrackStatus.GRANDCHILD)
                tracker.addGrandChild(newAnimal);

        animalCounter++;
        this.addGen(newAnimal.getGenotype());
    }

    public void updateAfterDeath(Animal dead) {
        if (dead.checkTrackStatus() != TrackStatus.NOT_TRACKED)
            tracker.removeFromTracked(dead, days);
        this.increaseDeaths();
        this.recalculateAvgLifespan(dead.getAge());
        this.removeGen(dead.getGenotype());
    }

    // Daily update of statistics after running simulation
    public void updateStatistics(ArrayList<Animal> animals) {
        days++;
        if (this.animalCounter != 0) {
            dominantGenotype = getDominantGen();
            int children = 0;
            int energy = 0;
            for (Animal animal : animals) {
                children += animal.getChildrenNumber();
                energy += animal.getEnergy();
            }
            this.avgChildrenNumber = (1.0 * children) / this.animalCounter;
            this.avgEnergyNumber = (1.0 * energy) / this.animalCounter;
        }
        updateGeneralStatistics();
    }

    // Daily update of general statistics after simulation
    private void updateGeneralStatistics() {
        generalStats.days++;
        generalStats.grassCounter += (this.grassCounter - generalStats.grassCounter) / days;
        generalStats.animalCounter += (this.animalCounter - generalStats.animalCounter) / days;
        generalStats.avgChildrenNumber = this.avgChildrenNumber;
        generalStats.avgEnergyNumber = this.avgEnergyNumber;
        generalStats.avgLifeSpan = this.avgLifeSpan;
        generalStats.addGen(this.getDominantGen());
        generalStats.dominantGenotype = generalStats.getDominantGen();
    }

    // Prepare String with current statistics report
    public String getCurrentStatsToPrint() {
        return getStatisticsString(this);
    }

    // Save general statistics to "report.txt"
    public void saveStats() {
        try {
            FileWriter writer = new FileWriter("report.txt");
            writer.write("REPORT\n" + this.getStatisticsString(this.generalStats));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set up new tracker
    public void startTracking(Animal tracked, int day) {
        this.tracker = new Tracker(tracked, day);
    }

    // Return tracking statistics and stop tracking
    public String getTrackingStats() {
        String stats = tracker.toString();
        tracker.stopTracking();
        return stats;
    }

    public int getDay() {
        return this.days;
    }

    public Genotype getDominantGenotype() {
        return dominantGenotype;
    }

    public void increaseGrasses(int newGrasses) {
        grassCounter += newGrasses;
    }

    public void decreaseGrassNumber() {
        grassCounter--;
    }

    private void increaseDeaths() {
        deathsCounter++;
    }

    private void recalculateAvgLifespan(int newAge) {
        avgLifeSpan += (newAge - avgLifeSpan) / deathsCounter;
    }

}