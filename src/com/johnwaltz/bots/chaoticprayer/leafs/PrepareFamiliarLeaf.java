package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.entities.SummonedFamiliar;
import com.runemate.game.api.script.framework.tree.LeafTask;

/**
 * Created by reece on 4/06/2017.
 */
public class PrepareFamiliarLeaf extends LeafTask {
    private ChaoticPrayer bot;

    public PrepareFamiliarLeaf(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        SummonedFamiliar familiar = Players.getLocal().getFamiliar();

        if (familiar == null) {
            if (Bank.isOpen() && Bank.contains(getPouchName())) {
                SpriteItem item = Bank.getItems(getPouchName()).first();
                Bank.withdraw(getPouchName(), 1);
                // todo
            }
        }
    }

    public String getPouchName() {
        return bot.chosenFamiliar + " pouch";
    }

    public String getPouchName(String familiar) {
        return familiar + " pouch";
    }
}
