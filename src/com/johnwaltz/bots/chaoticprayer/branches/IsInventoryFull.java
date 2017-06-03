package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.BankAction;
import com.johnwaltz.bots.chaoticprayer.leafs.EmptyLeaf;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 *
 */
public class IsInventoryFull extends BranchTask
{
    // Lazy instantiate our bot class
    private ChaoticPrayer bot;

    // Take in the bot in the constructor so we can reference the bot class
    public IsInventoryFull(ChaoticPrayer bot){
        this.bot = bot;
    }

    @Override
    public TreeTask successTask()
    {
        return new IsBankOpen(bot, BankAction.leaving);
    }

    @Override
    public boolean validate()
    {
        System.out.println("Branch IsInventoryFull: " + Inventory.isFull());
        return Inventory.isFull();
    }

    @Override
    public TreeTask failureTask()
    {
        // Not full,
        return new IsBankOpen(bot, BankAction.leaving);
    }
}
