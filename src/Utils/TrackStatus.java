package Utils;

import javafx.scene.paint.Color;

public enum TrackStatus {
    MAIN,           // Primarily chosen animal
    CHILD,          // Direct child of chosen animal
    GRANDCHILD,     // All offspring of child of main
    NOT_TRACKED;    // Not tracked

    public Color trackColor;

    static {
        MAIN.trackColor = Color.rgb(255, 130, 255); // nie lepiej te kolory wyrzucić gdzieś do GUI?
        CHILD.trackColor = Color.rgb(225, 40, 225);
        GRANDCHILD.trackColor = Color.rgb(153, 50, 204);
    }
}
