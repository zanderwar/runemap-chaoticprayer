package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.branches.IsAltarVisible;
import com.johnwaltz.bots.chaoticprayer.branches.IsSimonVisible;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.AbstractBot;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

import java.util.Objects;

/**
 * Created by reece on 3/06/2017.
 */
public class WithdrawBonesLeaf extends BranchTask {
    private ChaoticPrayer bot;

    public WithdrawBonesLeaf(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public TreeTask successTask() {
        return new IsAltarVisible(bot);
    }

    @Override
    public TreeTask failureTask() {
        if (!Bank.isOpen()) {
            return new IsSimonVisible(bot);
        }

        // not sure how it could get here
        System.out.println("Something went wrong...");
        bot.currentTask = "Something went wrong...";

        GameEvents.Universal.LOBBY_HANDLER.disable();
        RuneScape.logout();
        Environment.getBot().stop();

        return new EmptyLeaf();
    }

    @Override
    public boolean validate() {
        if (!Bank.isOpen()) {
            return false;
        }

        Bank.loadPreset((Objects.equals(bot.chosenPreset, "Preset 1")) ? 1 : 2);
        Execution.delayUntil(() -> (!Bank.isOpen()),500,10000);

        if (!Inventory.contains(bot.chosenBone)) {
            bot.currentTask = "No bones left to offer";

            GameEvents.Universal.LOBBY_HANDLER.disable();
            RuneScape.logout();
            Environment.getBot().stop();
        }

        return true;
    }
}
