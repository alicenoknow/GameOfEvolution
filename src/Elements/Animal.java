package Elements;

import Map.IPositionChangeObserver;
import Map.MapDirection;
import Map.WorldMap;
import javafx.scene.paint.Color;
import Utils.Config;
import Utils.TrackStatus;

import java.util.ArrayList;

public class Animal extends AbstractWorldMapElement {

    private MapDirection dir;
    private int energy;
    private final WorldMap map;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private final Genotype genotype;
    private int age;
    private int children;
    private TrackStatus trackStatus = TrackStatus.NOT_TRACKED;


    // Adam or Eve constructor
    public Animal(WorldMap map) {
        this.map = map;
        this.position = map.getInitialPos();
        this.dir = this.map.getRandomDirection();
        this.genotype = new Genotype();
        this.age = 0;
        this.energy = Config.getStartEnergy();
    }

    // Animal with parents constructor
    public Animal(Animal strongParent, Animal weakParent, Vector2d pos) {
        this.map = strongParent.map;
        this.position = pos;
        this.dir = this.map.getRandomDirection();
        this.genotype = new Genotype(strongParent.genotype, weakParent.genotype);
        this.age = 0;
        this.children = 0;
        this.energy = strongParent.energy / 4 + weakParent.energy / 4;

        // Update parents attributes
        strongParent.hasNewChild();
        weakParent.hasNewChild();
    }

    // Get rotation based on genes, change direction to given and take one step forward
    public void move(WorldMap map) {
        int rotation = this.genotype.getGeneticRotation();
        for (int i = 0; i < rotation; i++)
            dir = dir.next();

        Vector2d newPos = this.getPosition().add(dir.toUnitVector());
        newPos = map.wrapMap(newPos);
        Vector2d oldPos = this.getPosition();
        this.position = newPos;

        this.positionChanged(oldPos, newPos);
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : observers)
            observer.informPositionChanged(oldPosition, newPosition, this);
    }

    public void dies() {
        this.positionChanged(this.position, null);
        removeObserver(this.map);
    }

    public boolean canProcreate(Animal that) {
        return this.energy >= 0.5 * Config.getStartEnergy()
                && that.energy >= 0.5 * Config.getStartEnergy();
    }

    // Used to sort animals according to energy
    public int compareEnergy(Animal that) {
        if (this.energy > that.energy) return -1;
        else if (this.energy == that.energy) return 0;
        return 1;
    }

    // Decrease animal energy and increment age
    public void getOlder() {
        this.energy -= Config.getMoveEnergy();
        this.age++;
    }

    public void eat(int energy) {
        this.energy += energy;
    }

    // Update animals attributes after having a child
    public void hasNewChild() {
        this.energy -= this.energy / 4;
        this.children++;
    }

    // Return color, if animals is tracked(as main, child or grandchild) return suitable shade of violet
    // if it's not assign new color based on current energy
    public Color getColor() {
        if (trackStatus != TrackStatus.NOT_TRACKED)
            return trackStatus.trackColor;

        int energyRatio = Math.min(255,
                (int) ((Config.getStartEnergy()) * 1.0 / (this.energy + Config.getStartEnergy()) * 255));

        return Color.rgb(energyRatio, energyRatio, energyRatio);
    }

    // Get color if "show dominant genotype is selected"
    public Color getColor(Genotype dominant) {
        if(this.genotype == dominant)
            return Color.rgb(250, 255, 31);
        else return getColor();
    }


    public void changeTrackStatus(TrackStatus status) {
        this.trackStatus = status;
    }

    public TrackStatus checkTrackStatus() {
        return this.trackStatus;
    }

    public int getAge() {
        return this.age;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getChildrenNumber() {
        return this.children;
    }

    public Genotype getGenotype() {
        return this.genotype;
    }
}
