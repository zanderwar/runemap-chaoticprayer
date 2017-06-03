package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.TraversalLocation;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

/**
 *
 */
public class TraversalLeaf extends LeafTask {
    private ChaoticPrayer bot;
    private TraversalLocation location;
    private Area area;

    public TraversalLeaf(ChaoticPrayer bot, TraversalLocation location) {
        this.bot = bot;
        this.location = location;
    }

    @Override
    public void execute() {
        if (location == TraversalLocation.bankArea) {
            area = bot.bankArea;
        }
        else if (location == TraversalLocation.altarArea) {
            area = bot.altarArea;
        }
        else {
            area = bot.altarRegionArea;
        }

        final BresenhamPath bp = BresenhamPath.buildTo(area.getRandomCoordinate());

        if (bp != null) {
            if (bp.step(true)) {
                bot.currentTask = "Traversing to " + location;
                Execution.delayWhile(Players.getLocal()::isMoving, 1000, 2500);
            }
        }
    }
}
