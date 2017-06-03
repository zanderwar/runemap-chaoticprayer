package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class IsInventoryEmpty extends BranchTask {
    private ChaoticPrayer bot;

    public IsInventoryEmpty(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public TreeTask failureTask() {
        return new HaveBones(bot);
    }

    @Override
    public TreeTask successTask() {
        return new IsSimonVisible(bot);
    }

    @Override
    public boolean validate() {
        return Inventory.isEmpty();
    }
}
