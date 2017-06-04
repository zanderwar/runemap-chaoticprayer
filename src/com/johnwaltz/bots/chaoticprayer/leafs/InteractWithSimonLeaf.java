package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

/**
 * Created by reece on 4/06/2017.
 */
public class InteractWithSimonLeaf extends LeafTask {
    private GameObject obj;
    private ChaoticPrayer bot;

    public InteractWithSimonLeaf(ChaoticPrayer bot) {
        this.bot = bot;
    }

    public InteractWithSimonLeaf(ChaoticPrayer bot, GameObject obj) {
        this.bot = bot;
        this.obj = obj;
    }

    @Override
    public void execute() {
        if (obj == null || !obj.isValid()) {
            LocatableEntityQueryResults<GameObject> results = GameObjects.newQuery().names("Simon").actions("Bank").results();

            if (!results.isEmpty()) {
                obj = results.nearest();
            }
        }

        if (obj != null) {
            if(obj.interact("Bank")) {
                Execution.delayUntil(() -> (Bank.isOpen()), 1500, 3000);
            }
        }
    }
}
