package Elements;

import javafx.scene.paint.Color;

public class Grass extends AbstractWorldMapElement {

    public Grass(Vector2d pos) {
        this.position = pos;
    }

    public Color getColor() {
        return Color.rgb(12, 130, 40);
    }
}
