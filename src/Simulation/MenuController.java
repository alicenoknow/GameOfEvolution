package Simulation; // czy to na pewno jest element symulacji?

import Map.WorldMap;
import Utils.Config;
import Utils.InputParser;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class MenuController {
    @FXML
    private CheckBox fromFile;
    @FXML
    private TextField widthInput;
    @FXML
    private TextField heightInput;
    @FXML
    private TextField jungleRatioInput;
    @FXML
    private TextField initialEnergyInput;
    @FXML
    private TextField moveEnergyInput;
    @FXML
    private TextField grassEnergyInput;
    @FXML
    private TextField animalsInput;
    @FXML
    private RadioButton world1;
    @FXML
    private RadioButton world2;
    @FXML
    private Label message;


    @FXML
    // Get new Config from .json file or from direct input
    // Init given number of new simulations
    private void handleButtonAction() {
        InputParser parser = new InputParser();
        Config newConfig;

        if (fromFile.isSelected()) {
            newConfig = parser.getConfigFromJSON();
        } else {

            ArrayList<String> params = new ArrayList<>();
            params.add(widthInput.getText());
            params.add(heightInput.getText());
            params.add(jungleRatioInput.getText());
            params.add(initialEnergyInput.getText());
            params.add(moveEnergyInput.getText());
            params.add(grassEnergyInput.getText());
            params.add(animalsInput.getText());

            newConfig = parser.getConfigFromInput(params);
        }
        if (newConfig == null) {
            message.setText(" Wrong input! ");
            return;
        }

        if (world1.isSelected()) {
            SimulationEngine simulation = new SimulationEngine(new WorldMap());
        } else if (world2.isSelected()) {
            SimulationEngine simulation1 = new SimulationEngine(new WorldMap());
            SimulationEngine simulation2 = new SimulationEngine(new WorldMap());
        }
    }
}
