package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.Locatable;
import com.johnwaltz.bots.chaoticprayer.enums.TraversalLocation;
import com.johnwaltz.bots.chaoticprayer.leafs.TraversalLeaf;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 *
 */
public class IsNearby extends BranchTask {
    private ChaoticPrayer bot;

    private GameObject obj;
    private Locatable locatable;

    public IsNearby(ChaoticPrayer bot) {
        this.bot = bot;
    }

    public IsNearby(ChaoticPrayer bot, Locatable locatable) {
        this.bot = bot;
        this.locatable = locatable;
    }

    @Override
    public TreeTask failureTask() {
        if (locatable == Locatable.altar) {
            return new TraversalLeaf(bot, TraversalLocation.altarArea);
        } else if (locatable == Locatable.banker) {
            return new TraversalLeaf(bot, TraversalLocation.bankArea);
        }

        return new TraversalLeaf(bot, TraversalLocation.altarRegionArea);
    }

    @Override
    public TreeTask successTask() {
        return new HaveBones(bot);
    }

    @Override
    public boolean validate() {
        if (locatable == Locatable.altar) {
            obj = GameObjects.newQuery().names("Chaos altar").actions("Pray-at").results().nearest();
        } else if (locatable == Locatable.banker) {
            obj = GameObjects.newQuery().names("Simon").actions("Bank").results().nearest();
        } else {
            return bot.altarRegionArea.contains(Players.getLocal());
        }

        return obj != null && (obj.distanceTo(Players.getLocal()) < 12);
    }
}
