package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.EmptyLeaf;
import com.johnwaltz.bots.chaoticprayer.leafs.WaitUntilEmptyLeaf;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

import java.util.concurrent.TimeUnit;

/**
 * Class IsOffering
 *
 * Are we currently offering bones to the altar?
 */
public class IsOffering extends BranchTask {
    private ChaoticPrayer bot;
    private int bagCount;
    private WaitUntilEmptyLeaf waitUntilEmptyLeaf;
    private HaveBones haveBones;

    public IsOffering(ChaoticPrayer bot) {
        this.bot = bot;
        this.bagCount = Inventory.getItems(this.bot.chosenBone).size();
        this.waitUntilEmptyLeaf = new WaitUntilEmptyLeaf(bot);
        this.haveBones = new HaveBones(bot);
    }

    @Override
    public TreeTask failureTask() {
        return haveBones;
    }

    @Override
    public TreeTask successTask() {
        return waitUntilEmptyLeaf;
    }

    @Override
    public boolean validate() {
        if (bagCount == 0) {
            return false;
        }

        if (bot.altarArea.contains(Players.getLocal())) {
            return false;
        }

        return Execution.delayUntil(() -> (Inventory.getItems(bot.chosenBone).size() < bagCount), 3000, 5000);
    }
}
