package com.johnwaltz.bots.chaoticprayer.branches;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.johnwaltz.bots.chaoticprayer.enums.Locatable;
import com.johnwaltz.bots.chaoticprayer.enums.TraversalLocation;
import com.johnwaltz.bots.chaoticprayer.leafs.TraversalLeaf;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
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
    private IsBankOpen isBankOpen;
    private GameObject obj;
    private Locatable locatable;

    public IsNearby(ChaoticPrayer bot) {
        this.bot = bot;
    }

    public IsNearby(ChaoticPrayer bot, Locatable locatable) {
        this.bot = bot;
        this.locatable = locatable;
        this.isBankOpen = new IsBankOpen(bot);
    }

    @Override
    public TreeTask failureTask() {
        if (locatable == Locatable.altar) {
            return new TraversalLeaf(bot, TraversalLocation.altarArea);
        } else if (locatable == Locatable.banker) {
            return new TraversalLeaf(bot, TraversalLocation.bankArea);
        }

        return new TraversalLeaf(bot, TraversalLocation.bankArea);
    }

    @Override
    public TreeTask successTask() {
        return isBankOpen;
    }

    @Override
    public boolean validate() {
        LocatableEntityQueryResults<GameObject> results = null;

        if (locatable == Locatable.altar) {
            results = GameObjects.newQuery().names("Chaos altar").actions("Pray-at").results();
        } else if (locatable == Locatable.banker) {
            results = GameObjects.newQuery().names("Simon").actions("Bank").results();
        } else {
            return bot.altarRegionArea.contains(Players.getLocal());
        }

        if (!results.isEmpty()) {
            obj = results.nearest();

            if (obj == null) {
                return false;
            }
        }

        return obj != null && (obj.distanceTo(Players.getLocal()) < 12);
    }
}
