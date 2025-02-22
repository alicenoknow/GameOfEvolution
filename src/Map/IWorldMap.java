package Map;

import Elements.Animal;
import Elements.Vector2d;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that project.Elements.Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 */
public interface IWorldMap {

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     */
    void place(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);
}
