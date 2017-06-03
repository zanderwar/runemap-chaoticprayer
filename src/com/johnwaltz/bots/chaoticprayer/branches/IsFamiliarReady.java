package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.BankAction;
import com.johnwaltz.bots.chaoticprayer.leafs.PrepareFamiliarLeaf;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Item;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.entities.SummonedFamiliar;
import com.runemate.game.api.rs3.local.hud.interfaces.BeastOfBurden;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.Execution;
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
        return new PrepareFamiliarLeaf(bot);
    }

    @Override
    public TreeTask successTask() {
        return new IsBankOpen(bot, BankAction.leaving);
    }

    @Override
    public boolean validate() {
        SummonedFamiliar familiar = Players.getLocal().getFamiliar();
        if (familiar != null) {
            if (!Objects.equals(familiar.getName(), bot.chosenFamiliar) || Summoning.getMinutesRemaining() < 3) {
                Bank.depositInventoryOfBeastOfBurden();
                Bank.close();
                Execution.delayUntil(() -> (!Bank.isOpen()));
                familiar.interact("Dismiss");

                Execution.delayUntil(() -> (ChatDialog.getOption("Yes") != null));
                ChatDialog.getOption("Yes").select();
                Execution.delayUntil(() -> (Players.getLocal().getFamiliar() == null));

                GameObject obj = GameObjects.newQuery().names("Simon").actions("Bank").within(bot.bankArea).results().nearest();
                obj.interact("Bank");
                Execution.delayUntil(() -> (Bank.isOpen()));

                return false;
            };

            return true;
        }

        return false;
    }
}
