package Map;

import Elements.Animal;
import Elements.Grass;
import Elements.Herd;
import Elements.Vector2d;
import Utils.Config;

import java.util.*;

public class WorldMap extends BasicMap implements IWorldMap, IPositionChangeObserver {

    private final HashMap<Vector2d, Grass> grasses = new HashMap<>();
    private final HashMap<Vector2d, Herd> herds = new HashMap<>();

    private final Random generator = new Random();
    private final BasicMap jungle;

    public WorldMap() {
        super(0, 0, Config.getWidth() - 1, Config.getHeight() - 1);
        this.jungle = new BasicMap(Config.getJungleStartX(), Config.getJungleStartY(),
                Config.getJungleStartX() + Config.getJungleWidth() - 1,
                Config.getJungleStartY() + Config.getJungleHeight() - 1);
    }


    // Place animal on the map, start observing
    @Override
    public void place(Animal animal) {
        Vector2d animalPosition = animal.getPosition();
        if (!isOccupied(animalPosition)) {
            fieldsTaken += 1;
            if (jungle.hasPosition(animalPosition))
                jungle.fieldsTaken += 1;
        }
        addAnimalToMap(animalPosition, animal);
        animal.addObserver(this);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (herds.containsKey(position) || grasses.containsKey(position));
    }

    @Override
    public Object objectAt(Vector2d position) {
        Herd herd = herds.get(position);
        if (herd != null) return herd;
        return grasses.get(position);
    }

    // When animal moves, update herds and taken fields on the steppe and in the jungle
    @Override
    public void informPositionChanged(Vector2d oldPosition, Vector2d newPosition, Object object) {
        removeAnimalFromMap(oldPosition, (Animal) object);
        updateTakenFields(oldPosition, newPosition);
        if (newPosition != null)
            addAnimalToMap(newPosition, (Animal) object);
    }

    // Function verifies if animal by jumping from field to field released any position
    // both for whole map and jungle
    private void updateTakenFields(Vector2d oldPosition, Vector2d newPosition) {
        // If animal still takes a field
        if (newPosition != null) {
            // If old position is now free
            if (!isOccupied(oldPosition)) {
                // If new position, where animals heads to, is currently free
                if (!isOccupied(newPosition)) {
                    // If old position is in jungle, but new is not then jungle gains one free field
                    if (jungle.hasPosition(oldPosition) && !jungle.hasPosition(newPosition)) jungle.fieldsTaken -= 1;
                        // If old position is not in jungle, but new is then jungle loses one free field
                    else if (!jungle.hasPosition(oldPosition) && jungle.hasPosition(newPosition))
                        jungle.fieldsTaken += 1;
                    // If new position is already occupied
                } else {
                    // If old position is in jungle, then no matter where new is, jungle gains one free field
                    if (jungle.hasPosition(oldPosition)) jungle.fieldsTaken -= 1;
                    // If old position is occupied and new is, then whole map also gains free field
                    this.fieldsTaken -= 1;
                }
                // If old position is still occupied, but new position is not yet
            } else if (!isOccupied(newPosition)) {
                // If new is in the jungle, then jungle loses free field
                if (jungle.hasPosition(newPosition)) jungle.fieldsTaken += 1;
                // The whole map loses free field
                this.fieldsTaken += 1;
            }
        }
        // If animal died | because there is no new position
        // if old is now free, then both maps gain free field
        else if (!isOccupied(oldPosition)) {
            if (jungle.hasPosition(oldPosition)) jungle.fieldsTaken -= 1;
            this.fieldsTaken -= 1;
        }
    }

    // Adam or Eve initial position
    public Vector2d getInitialPos() {
        Vector2d initial;
        do
            initial = new Vector2d(generator.nextInt(endPoint.x + 1), generator.nextInt(endPoint.y + 1));
        while (objectAt(initial) instanceof Animal);
        return initial;
    }

    // Add animal to map, but not to observed
    private void addAnimalToMap(Vector2d position, Animal animal) {
        if (!herds.containsKey(position))
            herds.put(position, new Herd(animal, position));
        else herds.get(position).addMember(animal);
    }

    // Remove animal from animals list, but not from observed
    public void removeAnimalFromMap(Vector2d position, Animal animal) {
        if (herds.containsKey(position)) {
            Herd herdAtPosition = herds.get(position);
            if (herdAtPosition.countMembers() < 2)
                herds.remove(position);
            else herdAtPosition.removeMember(animal);
        }
    }

    public void removeGrassFromMap(Vector2d grassToRemove) {
        this.grasses.remove(grassToRemove);
    }

    // Grow two new grasses, one in the jungle, second on the steppe
    public int growGrasses() {
        int count = 0;
        if (this.placeGrass(getNewGrassPosSteppe())) {
            count++;
            this.fieldsTaken += 1;
        }
        if (this.placeGrass(getNewGrassPosJungle())) {
            count++;
            this.fieldsTaken += 1;
            jungle.fieldsTaken += 1;
        }
        return count;
    }

    protected boolean placeGrass(Vector2d pos) {
        if (pos == null)
            return false;
        Grass newGrass = new Grass(pos);
        this.grasses.put(newGrass.getPosition(), newGrass);
        return true;
    }


    // Get new grass position on steppe, no animals and no other grass
    private Vector2d getNewGrassPosSteppe() {
        if (this.isFull())
            return null;

        Vector2d pos;
        if (this.isAlmostFull())
            pos = findFirstFreePosBetween(this.startPoint, this.endPoint);
        else
            do
                pos = findFreeRandomPosBetween(this.startPoint, this.endPoint);
            while (jungle.hasPosition(pos));

        return pos;
    }

    // Get new position in jungle, no animals, no other grass
    private Vector2d getNewGrassPosJungle() {
        if (jungle.isFull())
            return null;

        if (jungle.isAlmostFull())
            return findFirstFreePosBetween(jungle.startPoint, jungle.endPoint);
        else
            return findFreeRandomPosBetween(jungle.startPoint, jungle.endPoint);
    }


    // In case animal wants to make a step outside the map, change its position as if the map was wrapped
    public Vector2d wrapMap(Vector2d position) {
        if (this.hasPosition(position)) return position;
        int w = this.endPoint.x + 1;
        int h = this.endPoint.y + 1;
        return new Vector2d((w + position.x) % w, (h + position.y) % h);
    }

    public MapDirection getRandomDirection() {
        int turns = generator.nextInt(8);
        MapDirection rand = MapDirection.NORTH;
        for (int i = 0; i < turns; i++)
            rand = rand.next();
        return rand;
    }

    // Return list of current herds with their current food supply (grass on the territory of the herd)
    public LinkedList<Herd> getHerds() {
        LinkedList<Herd> herdsList = new LinkedList<>();
        for (Herd newHerd : herds.values()) {
            if (this.grasses.containsKey(newHerd.getPosition()))
                newHerd.addGrass(this.grasses.get(newHerd.getPosition()));
            herdsList.add(newHerd);
        }
        return herdsList;
    }

    // Find free position in rectangle given by vectors by random picking
    private Vector2d findFreeRandomPosBetween(Vector2d start, Vector2d end) {
        int x, y;
        Vector2d pos;
        do {
            x = generator.nextInt(end.x - start.x + 1);
            y = generator.nextInt(end.y - start.y + 1);
            pos = new Vector2d(start.x + x, start.y + y);
        } while (isOccupied(pos));
        return pos;
    }

    // Find free position in rectangle given by vectors by iterating over it
    private Vector2d findFirstFreePosBetween(Vector2d start, Vector2d end) {
        Vector2d pos;
        for (int i = start.x; i <= start.x; i++)
            for (int j = start.y; j <= end.y; j++) {
                pos = new Vector2d(i, j);
                if (!isOccupied(pos))
                    return pos;
            }
        return null;
    }

    // Find position for new child - next to parent
    public Vector2d findChildPosition(Vector2d parentPos) {
        Vector2d childPosition;
        MapDirection dir = getRandomDirection();
        int i = 0;
        do {
            childPosition = parentPos.add(dir.toUnitVector());
            dir = dir.next();
            i++;
        } while (isOccupied(childPosition) && i < 7);

        return childPosition;
    }

}
