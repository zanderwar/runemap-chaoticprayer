package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.EmptyLeaf;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 *
 */
public class RootBranch extends BranchTask {
    private ChaoticPrayer bot;
    private EmptyLeaf emptyLeaf = new EmptyLeaf();
    private IsNearby isNearby;

    public RootBranch(ChaoticPrayer bot) {
        this.bot = bot;
        this.isNearby = new IsNearby(bot);
    }

    @Override
    public TreeTask successTask() {
        return emptyLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return isNearby;
    }

    @Override
    public boolean validate() {
        bot.updateCurrentTask("Waiting on GUI");

        return bot.guiWait;
    }
}
