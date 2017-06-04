package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.leafs.FamiliarPreparation;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.entities.SummonedFamiliar;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

import java.util.Objects;

/**
 * Created by reece on 4/06/2017.
 */
public class IsFamiliarReady extends BranchTask {
    private ChaoticPrayer bot;

    public IsFamiliarReady(ChaoticPrayer bot) {
        this.bot = bot;
    }
    @Override
    public TreeTask failureTask() {
        return new FamiliarPreparation(bot);
    }

    @Override
    public TreeTask successTask() {
        return new IsBankOpen(bot);
    }

    @Override
    public boolean validate() {
        SummonedFamiliar familiar = Players.getLocal().getFamiliar();
        if (familiar != null) {
            if (!Objects.equals(familiar.getName(), bot.chosenFamiliar) || Summoning.getMinutesRemaining() < 3) {
                return false;
            };

            return true;
        }

        return false;
    }
}
