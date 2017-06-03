package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.BankAction;
import com.johnwaltz.bots.chaoticprayer.enums.TraversalLocation;
import com.johnwaltz.bots.chaoticprayer.leafs.TraversalLeaf;
import com.johnwaltz.bots.chaoticprayer.leafs.WithdrawBonesLeaf;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class IsBankOpen extends BranchTask {
    private ChaoticPrayer bot;
    private BankAction bankAction;

    public IsBankOpen(ChaoticPrayer bot, BankAction bankAction) {
        this.bot = bot;
        this.bankAction = bankAction;
    }

    @Override
    public TreeTask failureTask() {
        if (bankAction == BankAction.withdrawing) {
            return new IsSimonVisible(bot);
        }

        return new HaveBones(bot);
    }

    @Override
    public TreeTask successTask() {
        if (bankAction == BankAction.leaving) {
            Bank.close();
            Execution.delayUntil(() -> (!Bank.isOpen()));

            return new IsAltarVisible(bot);
        }

        return new WithdrawBonesLeaf(bot);
    }

    @Override
    public boolean validate() {
        return Bank.isOpen();
    }
}
