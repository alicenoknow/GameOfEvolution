package Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Config {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private static int width;
    private static int height;
    private static double jungleRatio;
    private static int moveEnergy;
    private static int startEnergy;
    private static int plantEnergy;
    private static int numberOfAnimals;
    private static int jungleStartX;
    private static int jungleStartY;
    private static int jungleWidth;
    private static int jungleHeight;

    public Config(ArrayList<String> params) {
        try {
            width = Integer.parseInt(params.get(0));
            height = Integer.parseInt(params.get(1));
            jungleRatio = Double.parseDouble(params.get(2));
            startEnergy = Integer.parseInt(params.get(3));
            moveEnergy = Integer.parseInt(params.get(4));
            plantEnergy = Integer.parseInt(params.get(5));
            numberOfAnimals = Integer.parseInt(params.get(6));
            calculateJungle();
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    public Config() {
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser().parse(new FileReader("params.json")));
            width = ((Long) jsonObject.get("width")).intValue();
            height = ((Long) jsonObject.get("height")).intValue();
            jungleRatio = ((Number) jsonObject.get("jungleRatio")).doubleValue();
            moveEnergy = ((Long) jsonObject.get("moveEnergy")).intValue();
            startEnergy = ((Long) jsonObject.get("startEnergy")).intValue();
            plantEnergy = ((Long) jsonObject.get("plantEnergy")).intValue();
            numberOfAnimals = ((Long) jsonObject.get("numberOfAnimals")).intValue();
            calculateJungle();

        } catch (IOException | NullPointerException | IllegalArgumentException | ParseException e) {
            log.log(Level.WARNING, e.getMessage());
        }

    }

    private void calculateJungle() {
        jungleWidth = (int) ((width) * Math.sqrt(jungleRatio));
        jungleHeight = (int) ((height) * Math.sqrt(jungleRatio));
        jungleStartX = (width - jungleWidth) / 2;
        jungleStartY = (height - jungleHeight) / 2;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static double getJungleRatio() {
        return jungleRatio;
    }

    public static int getMoveEnergy() {
        return moveEnergy;
    }

    public static int getStartEnergy() {
        return startEnergy;
    }

    public static int getPlantEnergy() {
        return plantEnergy;
    }

    public static int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public static int getJungleStartX() {
        return jungleStartX;
    }

    public static int getJungleStartY() {
        return jungleStartY;
    }

    public static int getJungleWidth() {
        return jungleWidth;
    }

    public static int getJungleHeight() {
        return jungleHeight;
    }
}
