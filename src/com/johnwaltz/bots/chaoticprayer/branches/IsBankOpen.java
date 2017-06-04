package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.FamiliarPreparation;
import com.johnwaltz.bots.chaoticprayer.leafs.WithdrawBonesLeaf;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class IsBankOpen extends BranchTask {
    private ChaoticPrayer bot;

    public IsBankOpen(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public TreeTask failureTask() {
        return new IsOffering(bot);
    }

    @Override
    public TreeTask successTask() {
        if ((bot.useFamiliar && bot.chosenFamiliar != null) && (Players.getLocal().getFamiliar() == null || Summoning.getMinutesRemaining() < 3)) {
            return new FamiliarPreparation(bot);
        }

        return new WithdrawBonesLeaf(bot);
    }

    @Override
    public boolean validate() {
        return Bank.isOpen();
    }
}
