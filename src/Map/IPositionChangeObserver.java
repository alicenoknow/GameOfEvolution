package Map;

import Elements.Vector2d;

public interface IPositionChangeObserver {
    void informPositionChanged(Vector2d oldPosition, Vector2d newPosition, Object object);

}
