package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.EmptyLeaf;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class Root extends BranchTask {
    private ChaoticPrayer bot;

    public Root(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public TreeTask successTask() {
        return new EmptyLeaf();
    }

    @Override
    public boolean validate() {
        bot.currentTask = "Waiting on GUI";
        bot.updateInfo();
        return bot.guiWait;
    }

    @Override
    public TreeTask failureTask() {
        bot.player = Players.getLocal();
        bot.updateInfo();

        return new IsNearby(bot);
    }
}
