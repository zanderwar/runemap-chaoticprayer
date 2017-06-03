package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.EmptyLeaf;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by reece on 3/06/2017.
 */
public class IsOffering extends BranchTask {
    private ChaoticPrayer bot;
    private int bagCount;

    public IsOffering(ChaoticPrayer bot) {
        this.bot = bot;
        this.bagCount = Inventory.getItems().size();
    }

    @Override
    public TreeTask failureTask() {
        return new IsInventoryEmpty(bot);
    }

    @Override
    public TreeTask successTask() {
        Execution.delayUntil(() -> (Inventory.isEmpty()), 1000, 30000);
        return new IsInventoryEmpty(bot);
    }

    @Override
    public boolean validate() {
        if (bagCount == 0) {
            return false;
        }

        Execution.delayUntil(() -> (Inventory.getItems().size() < bagCount), 4000, 6500);

        return true;
    }
}
