package com.johnwaltz.bots.chaoticprayer;

import com.johnwaltz.bots.chaoticprayer.ui.ChaoticFXController;
import com.johnwaltz.bots.chaoticprayer.ui.Info;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.CommonMath;
import com.runemate.game.api.script.framework.core.LoopingThread;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.tree.TreeBot;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.johnwaltz.bots.chaoticprayer.branches.RootBranch;
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
    public ChaoticFXController configUI = null;
    public String currentTask;
    public boolean guiWait;
    public String chosenBone;
    public String chosenPreset;
    public boolean useFamiliar;
    public String chosenFamiliar;
    public int startingPrayerLevel;
    public int startingPrayerXp;
    private int boneCount;
    private SimpleObjectProperty<Node> botInterfaceProperty;
    private LoopingThread updateUi = new LoopingThread(this::updateInfo, 500);

    private StopWatch stopWatch = new StopWatch();

    public Area altarRegionArea = new Area.Rectangular(new Coordinate(3230, 3621, 0), new Coordinate(3249, 3596, 0)),
            altarArea = new Area.Rectangular(new Coordinate(3233, 3605, 0), new Coordinate(3243, 3596, 0)),
            bankArea = new Area.Rectangular(new Coordinate(3240, 3610, 0), new Coordinate(3239, 3609, 0));

    public ChaoticPrayer() {
        this.guiWait = true;
        this.boneCount = 0;

        this.startingPrayerLevel = Skill.PRAYER.getBaseLevel();
        this.startingPrayerXp = Skill.PRAYER.getExperience();

        setEmbeddableUI(this);
    }

    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (this.botInterfaceProperty == null) {
            this.botInterfaceProperty = new SimpleObjectProperty(configUI = new ChaoticFXController(this));
        }

        return this.botInterfaceProperty;
    }

    @Override
    public TreeTask createRootTask() {
        return new RootBranch(this);
    }

    /**
     * @param event Event
     */
    public void onItemRemoved(ItemEvent event) {
        ItemDefinition definition = event.getItem().getDefinition();
        if (definition != null) {
            if (definition.getName().equals(chosenBone)) {
                this.boneCount++;
            }
        }
    }

    @Override
    public void onStart(String... args) {
        this.stopWatch.start();
        this.updateCurrentTask("Starting bot ...");
        this.updateUi.start();

        setLoopDelay(300, 600);

        getEventDispatcher().addListener(this);
    }

    public void updateInfo() {
        try {
            int xpGained = 0;
            int levelsGained = 0;
            if (Players.getLocal() != null) {
                xpGained = Skill.PRAYER.getExperience() - this.startingPrayerXp;
                levelsGained = Skill.PRAYER.getBaseLevel() - this.startingPrayerLevel;
            }

            info = new Info(
                    (int) CommonMath.rate(TimeUnit.HOURS, stopWatch.getRuntime(), boneCount),
                    this.boneCount,
                    this.stopWatch.getRuntimeAsString(),
                    this.currentTask,
                    CommonMath.rate(TimeUnit.HOURS, this.stopWatch.getRuntime(), xpGained),
                    levelsGained
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (configUI != null) {
            Platform.runLater(() -> configUI.updateInfo());
        }
    }

    public void updateCurrentTask(String message) {
        this.currentTask = message;

        // todo write to a TextArea
    }
}
