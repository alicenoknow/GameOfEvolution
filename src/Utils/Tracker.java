package Utils;

import Elements.Animal;

import java.util.ArrayList;

public class Tracker {
    private Animal trackedAnimal;
    private int deathDay = 0;
    private final int trackDay;
    private ArrayList<Animal> trackedChildren = new ArrayList<>();
    private ArrayList<Animal> trackedGrandChildren = new ArrayList<>(); // a dalsze pokolenia?

    public Tracker(Animal animal, int trackDay) {
        this.trackedAnimal = animal;
        this.trackDay = trackDay;
        animal.changeTrackStatus(TrackStatus.MAIN);
    }

    // Start tracking child of main tracked animal
    public void addChild(Animal animal) {
        trackedChildren.add(animal);
        animal.changeTrackStatus(TrackStatus.CHILD);
    }

    // Start tracking offspring of children of main tracked animal
    public void addGrandChild(Animal animal) {
        trackedGrandChildren.add(animal);
        animal.changeTrackStatus(TrackStatus.GRANDCHILD);
    }

    public void removeFromTracked(Animal animal, int today) {
        if (animal == trackedAnimal)
            deathDay = today;
        else if (trackedChildren.contains(animal)) trackedChildren.remove(animal);
        else trackedGrandChildren.remove(animal);
        animal.changeTrackStatus(TrackStatus.NOT_TRACKED);
    }

    // Stop tracking all animals
    public void stopTracking() {
        this.trackedAnimal.changeTrackStatus(TrackStatus.NOT_TRACKED);
        this.trackedAnimal = null;
        deathDay = 0;
        for (Animal animal : trackedChildren) animal.changeTrackStatus(TrackStatus.NOT_TRACKED);
        for (Animal animal : trackedGrandChildren) animal.changeTrackStatus(TrackStatus.NOT_TRACKED);
        this.trackedChildren = null;
        this.trackedGrandChildren = null;
    }

    // Final tracking statistics
    @Override
    public String toString() {
        String toString = String.format("On day %d:\n", trackDay);  // StringBuilder
        if (deathDay > 0)
            toString += String.format("Chosen animal died on %d day.\n", deathDay);
        else toString += "Chosen animal is alive.\n";
        toString += String.format("Alive children number: %d\nAll alive grandchildren number: %d",
                trackedChildren.size(), trackedGrandChildren.size());
        return toString;
    }
}
