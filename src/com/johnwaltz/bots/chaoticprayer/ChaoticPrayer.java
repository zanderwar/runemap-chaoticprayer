package com.johnwaltz.bots.chaoticprayer;

import com.johnwaltz.bots.chaoticprayer.ui.ChaoticFXGui;
import com.johnwaltz.bots.chaoticprayer.ui.Info;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.CommonMath;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.tree.TreeBot;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.johnwaltz.bots.chaoticprayer.branches.Root;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.util.concurrent.TimeUnit;

/**
 * Class Chaotic Prayer
 *
 * @author Reece Alexander
 */
public class ChaoticPrayer extends TreeBot implements InventoryListener, EmbeddableUI {

    public Info info;
    public ChaoticFXGui configUI;
    public String currentTask;
    public boolean guiWait;
    public Player player;
    public String chosenBone;
    public String chosenPreset;
    public String chosenFamiliar;

    private int boneCount;
    private SimpleObjectProperty<Node> botInterfaceProperty;
    private StopWatch stopWatch = new StopWatch();

    public Area altarRegionArea = new Area.Rectangular(new Coordinate(3230, 3621, 0), new Coordinate(3249, 3596, 0)),
            altarArea = new Area.Rectangular(new Coordinate(3233, 3605, 0), new Coordinate(3243, 3596, 0)),
            bankArea = new Area.Rectangular(new Coordinate(3240, 3610, 0), new Coordinate(3239, 3609, 0));

    public ChaoticPrayer() {
        guiWait = true;
        boneCount = 0;

        setEmbeddableUI(this);
    }

    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            botInterfaceProperty = new SimpleObjectProperty(configUI = new ChaoticFXGui(this));
        }

        return botInterfaceProperty;
    }

    @Override
    public TreeTask createRootTask() {
        return new Root(this);
    }

    /**
     * @param event Event
     */
    public void onItemRemoved(ItemEvent event) {
        ItemDefinition definition = event.getItem().getDefinition();
        if (definition != null) {
            if (definition.getName().toLowerCase().contains("bones") || definition.getName().toLowerCase().contains("ashes")) {
                boneCount++;
            }
        }
    }

    @Override
    public void onStart(String... args) {
        // Set/Run anything that needs to be ran at the initial start of the bot
        stopWatch.start();
        currentTask = "Starting bot...";

        // Sets the length of time in milliseconds to wait before calling onLoop again
        // NOTE: IT IS NOT RECOMMENDED TO KEEP DEFAULT LOOP DELAY
        setLoopDelay(300, 600);

        // Set custom mouse multiplier or leave default
        Mouse.setSpeedMultiplier(1);

        // Force menu interaction when clicking (force right-click interaction)
        // Mouse.setForceMenuInteraction(true);

        // Add this class as a listener for the Event Dispatcher
        getEventDispatcher().addListener(this);
    }

    public void updateInfo() {
        try {
            info = new Info(
                    (int) CommonMath.rate(TimeUnit.HOURS, stopWatch.getRuntime(), boneCount),
                    boneCount,
                    stopWatch.getRuntimeAsString(),
                    currentTask
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> configUI.updateInfo());
    }
}
