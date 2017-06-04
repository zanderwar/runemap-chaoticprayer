package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

import java.util.Objects;

/**
 * Class WithdrawBonesLeaf
 *
 * Withdraws the required bones from the bank, if the preset withdraws no bones to the inventory,
 * the client will be logged out and the bot will be stopped.
 */
public class WithdrawBonesLeaf extends LeafTask {
    private ChaoticPrayer bot;

    public WithdrawBonesLeaf(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        if (!Bank.isOpen()) {
            return;
        }

        if (Bank.loadPreset((Objects.equals(bot.chosenPreset, "Preset 1")) ? 1 : 2)) {
            if (Execution.delayUntil(() -> (!Bank.isOpen()), 500, 10000)) {
                if (!Inventory.contains(bot.chosenBone)) {
                    bot.currentTask = "That preset withdrew no bones";

                    GameEvents.Universal.LOBBY_HANDLER.disable();
                    RuneScape.logout();
                    Environment.getBot().stop();
                }
            }
        };
    }
}
