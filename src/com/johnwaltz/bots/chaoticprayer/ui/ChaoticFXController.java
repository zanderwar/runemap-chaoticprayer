package com.johnwaltz.bots.chaoticprayer.ui;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.util.Resources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Chaotic Prayer FX Controller
 */
public class ChaoticFXController extends GridPane implements Initializable {
    @FXML
    private ComboBox chosenBone, chosenPreset, chosenFamiliar;

    @FXML
    private CheckBox useFamiliar;

    @FXML
    private Button actionStartButton;

    @FXML
    private Label currentTaskLabel, runtimeLabel;

    @FXML
    private Slider speedMultiplierSlider;

    @FXML
    private TextField speedMultiplierValue;


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

        FXMLLoader loader = new FXMLLoader();

        Future<InputStream> stream = bot.getPlatform().invokeLater(() -> Resources.getAsStream("com/johnwaltz/bots/chaoticprayer/ui/ChaoticPrayer.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load(stream.get());
        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("Error loading GUI");
            e.printStackTrace();
        }
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
        chosenFamiliar.setOnAction(eventChosenFamiliarComboxBox());
        chosenBone.setOnAction(eventChosenBoneComboxBox());
        actionStartButton.setOnAction(eventStart());
    }

    private EventHandler<ActionEvent> eventChosenFamiliarComboxBox() {
        return event -> {
            actionStartButton.setDisable(!isBotReady());
        };
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
                Mouse.setSpeedMultiplier(speedMultiplierSlider.getValue());
                if (useFamiliar.isSelected()) {
                    bot.useFamiliar = true;
                    bot.chosenFamiliar = chosenFamiliar.getSelectionModel().getSelectedItem().toString();
                } else {
                    bot.useFamiliar = false;
                    bot.chosenFamiliar = null;
                }
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

    private boolean isBotReady() {
        boolean ready = true;

        if (chosenBone.getSelectionModel().getSelectedItem() == null) {
            ready = false;
        }

        if (chosenPreset.getSelectionModel().getSelectedItem() == null) {
            ready = false;
        }

        if (!useFamiliar.isSelected() || useFamiliar.isSelected() && chosenFamiliar.getSelectionModel().getSelectedItem() == null) {
            ready = false;
        }

        return ready;
    }

    public void updateInfo() {
        Info i = bot.info;

        if (i != null) {
            currentTaskLabel.setText(i.currentTask);
            runtimeLabel.setText(i.runTime);
        }
    }
}
