package Map;

import Elements.Vector2d;

public class BasicMap {
    protected final Vector2d startPoint;
    protected final Vector2d endPoint;
    protected final int mapCapacity;
    protected int fieldsTaken;

    public BasicMap(int lowerX, int lowerY, int upperX, int upperY) {
        this.startPoint = new Vector2d(lowerX, lowerY);
        this.endPoint = new Vector2d(upperX, upperY);
        this.mapCapacity = (endPoint.y - startPoint.y + 1) * (endPoint.x - startPoint.x + 1);
        this.fieldsTaken = 0;
    }

    // Full means - all positions are occupied
    protected boolean isFull() {
        return this.fieldsTaken == this.mapCapacity;
    }

    // I assume, if map is more than 90% full, iteration will make more sense than randomly picking
    protected boolean isAlmostFull() {
        return this.fieldsTaken >= 0.9 * this.mapCapacity;
    }

    // Check if position is in map bounds
    protected boolean hasPosition(Vector2d pos) {
        return (pos.follows(this.startPoint) && pos.precedes(this.endPoint));
    }
}
