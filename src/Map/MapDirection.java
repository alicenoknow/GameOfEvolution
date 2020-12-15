package Map;

import Elements.Vector2d;

public enum MapDirection {

    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    private MapDirection next;
    private Vector2d vector;

    static {
        NORTH.next = NORTH_EAST;
        NORTH_EAST.next = EAST;
        EAST.next = SOUTH_EAST;
        SOUTH_EAST.next = SOUTH;
        SOUTH.next = SOUTH_WEST;
        SOUTH_WEST.next = WEST;
        WEST.next = NORTH_WEST;
        NORTH_WEST.next = NORTH;
    }

    static {
        NORTH.vector = new Vector2d(0, 1);
        NORTH_EAST.vector = new Vector2d(1, 1);
        NORTH_WEST.vector = new Vector2d(-1, 1);
        SOUTH_EAST.vector = new Vector2d(1, -1);
        SOUTH_WEST.vector = new Vector2d(-1, -1);
        SOUTH.vector = new Vector2d(0, -1);
        EAST.vector = new Vector2d(1, 0);
        WEST.vector = new Vector2d(-1, 0);
    }


    public MapDirection next() {
        return next;
    }

    public Vector2d toUnitVector() {
        return vector;
    }

}
