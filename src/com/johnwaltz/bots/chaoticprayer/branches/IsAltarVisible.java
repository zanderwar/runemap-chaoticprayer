package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.TraversalLocation;
import com.johnwaltz.bots.chaoticprayer.leafs.TraversalLeaf;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class IsAltarVisible extends BranchTask {
    private ChaoticPrayer bot;
    private GameObject obj;
    private boolean nearby;

    public IsAltarVisible(ChaoticPrayer bot) {
        this.nearby = true;
        this.bot = bot;
    }

    @Override
    public TreeTask failureTask() {
        if (!nearby) {
            return new TraversalLeaf(bot, TraversalLocation.altarArea);
        }

        Camera.concurrentlyTurnTo(obj);

        return successTask();
    }

    @Override
    public TreeTask successTask() {
        obj.interact("Pray-at");
        Execution.delay(1000,3000);
        Execution.delayWhile(Players.getLocal()::isMoving);

        return new IsOffering(bot);
    }

    @Override
    public boolean validate() {
        obj = GameObjects.newQuery().names("Chaos altar").actions("Pray-at").results().nearest();

        if (obj == null) {
            nearby = false;
            return false;
        }

        return obj.isVisible();
    }
}
