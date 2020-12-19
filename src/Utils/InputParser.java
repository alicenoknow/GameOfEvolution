package Utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputParser {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public Config getConfigFromJSON(){
        Config config = new Config();
        if (verifyParameters())
            return config;
        return null;
    }

    public Config getConfigFromInput(ArrayList<String> params) {
        Config config = new Config(params);
        if (verifyParameters())
            return config;
        return null;
    }

    // Verify all parameters
    private boolean verifyParameters() {
        try {
            verifyPositiveNonZero(Config.getWidth());
            verifyPositiveNonZero(Config.getHeight());
            verifyRatio(Config.getJungleRatio());
            verifyPositive(Config.getStartEnergy());
            verifyPositive(Config.getMoveEnergy());
            verifyPositive(Config.getPlantEnergy());
            verifyPositiveNonZero(Config.getNumberOfAnimals());
            verifySpace(Config.getWidth(), Config.getHeight(), Config.getNumberOfAnimals());
        } catch (IllegalArgumentException e) {
            log.log(Level.WARNING, e.getMessage());
            return false;
        }
        return true;
    }

    private void verifyPositive(int valInt) throws IllegalArgumentException {
        if (valInt < 0)
            throw new IllegalArgumentException("Negative number");
    }

    private void verifyPositiveNonZero(int valInt) throws IllegalArgumentException {
        if (valInt <= 0)
            throw new IllegalArgumentException("Zero or negative number");
    }

    private void verifySpace(int width, int height, int animals) throws IllegalArgumentException {
        if (width * height < animals)
            throw new IllegalArgumentException("No space for animals!");
    }

    private void verifyRatio(double ratio) throws IllegalArgumentException{
        if (ratio < 0)
            throw new IllegalArgumentException("Negative ratio!");
        if (ratio > 1)
            throw new IllegalArgumentException("Ratio bigger than one!");
    }

}
