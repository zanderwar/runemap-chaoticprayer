package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.InteractWithAltarLeaf;
import com.johnwaltz.bots.chaoticprayer.leafs.InteractWithSimonLeaf;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Class HaveBones
 *
 * Do we have the correct bones?
 * - true: Move to IsAltarVisible branch
 * - false: Move to IsSimonVisible branch
 */
public class HaveBones extends BranchTask {
    private ChaoticPrayer bot;

    public HaveBones(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public TreeTask failureTask() {
        return new InteractWithSimonLeaf(bot);
    }

    @Override
    public TreeTask successTask() {
        return new InteractWithAltarLeaf(bot);
    }

    @Override
    public boolean validate() {
        return Inventory.contains(bot.chosenBone);
    }
}
