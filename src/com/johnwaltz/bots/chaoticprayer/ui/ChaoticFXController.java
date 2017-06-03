package com.johnwaltz.bots.chaoticprayer.ui;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Chaotic Prayer FX Controller
 */
public class ChaoticFXController implements Initializable {
    @FXML
    private ComboBox chosenBone;

    @FXML
    private ComboBox chosenPreset;

    @FXML
    private CheckBox useFamiliar;

    @FXML
    private ComboBox chosenFamiliar;

    @FXML
    private Button actionStartButton;

    @FXML
    private Label currentTaskLabel;

    /**
     * The bot instance
     */
    private ChaoticPrayer bot;

    /**
     * Constructor
     *
     * @param bot The bot instance
     */
    public ChaoticFXController(ChaoticPrayer bot) {
        this.bot = bot;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chosenBone.getItems().addAll(
                "Bones",
                "Wolf bones",
                "Burnt bones",
                "Monkey bones",
                "Bat bones",
                "Big bones",
                "Jogre bones",
                "Zogre bones",
                "Shaikahan bones",
                "Baby dragon bones",
                "Wyvern bones",
                "Dragon bones",
                "Fayrg bones",
                "Raurg bones",
                "Dagannoth bones",
                "Airut bones",
                "Ourg bones",
                "Hardened dragon bones",
                "Frost dragon bones",
                "Reinforced dragon bones"
        );

        chosenFamiliar.getItems().addAll(
                "War tortoise",
                "Pack yak"
        );

        chosenPreset.getItems().addAll(
                "Preset 1",
                "Preset 2"
        );

        chosenPreset.setOnAction(eventChosenPresetComboBox());
        useFamiliar.setOnAction(eventUseFamiliarCheckBox());
        chosenBone.setOnAction(eventChosenBoneComboxBox());
        actionStartButton.setOnAction(eventStart());
    }

    private EventHandler<ActionEvent> eventChosenPresetComboBox() {
        return event -> {
            actionStartButton.setDisable(!isBotReady());
        };
    }

    private EventHandler<ActionEvent> eventUseFamiliarCheckBox() {
        return event -> {
            chosenFamiliar.setDisable(!useFamiliar.isSelected());
            actionStartButton.setDisable(!isBotReady());
        };
    }

    /**
     * @return
     */
    public EventHandler<ActionEvent> eventStart() {
        return event -> {
            try {
                bot.chosenBone = chosenBone.getSelectionModel().getSelectedItem().toString();
                bot.chosenPreset = chosenPreset.getSelectionModel().getSelectedItem().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * @return
     */
    private EventHandler<ActionEvent> eventChosenBoneComboxBox() {
        return event -> {
            actionStartButton.setDisable(!isBotReady());
        };
    }

    private void disableStartButton(boolean state) {
        actionStartButton.setDisable(false);
    }

    private boolean isBotReady() {
        boolean ready = true;

        if (chosenBone.getSelectionModel().getSelectedItem() == null) {
            ready = false;
        }

        if (chosenPreset.getSelectionModel().getSelectedItem() == null) {
            ready = false;
        }

        if (useFamiliar.isSelected() && chosenFamiliar.getSelectionModel().getSelectedItem() == null) {
            ready = false;
        }

        return ready;
    }

    public void update() {
        Info i = bot.info;

        currentTaskLabel.textProperty().set("" + i.currentTask);
    }
}
