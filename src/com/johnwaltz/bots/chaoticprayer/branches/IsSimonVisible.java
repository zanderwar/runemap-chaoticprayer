package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.BankAction;
import com.johnwaltz.bots.chaoticprayer.enums.TraversalLocation;
import com.johnwaltz.bots.chaoticprayer.leafs.TraversalLeaf;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class IsSimonVisible extends BranchTask {
    private ChaoticPrayer bot;
    private GameObject obj;
    private boolean nearby = true;

    public IsSimonVisible(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public TreeTask failureTask() {
        if (!nearby) {
            return new TraversalLeaf(bot, TraversalLocation.bankArea);
        }

        Camera.concurrentlyTurnTo(obj);
        obj.interact("Bank");
        Execution.delayUntil(() -> (Bank.isOpen()), 1000, 15000);
        return new IsBankOpen(bot, BankAction.withdrawing);
    }

    @Override
    public TreeTask successTask() {
        return null;
    }

    @Override
    public boolean validate() {
        obj = GameObjects.newQuery().names("Simon").actions("Bank").results().nearest();

        if (obj == null) {
            nearby = false;
            return false;
        }

        return obj.isVisible();
    }
}
