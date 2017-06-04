package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

/**
 * Class WaitUntilEmptyLeaf
 *
 * A leaf purely to wait until the inventory is empty, you only make it here if you're currently offering
 * bones to the altar
 */
public class WaitUntilEmptyLeaf extends LeafTask {
    private ChaoticPrayer bot;

    public WaitUntilEmptyLeaf(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        bot.updateCurrentTask("Offering bones...");
        Execution.delayUntil(() -> (!Inventory.contains(bot.chosenBone)), 1000, 30000);
    }
}
