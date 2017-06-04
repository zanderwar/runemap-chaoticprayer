package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

/**
 * Created by reece on 4/06/2017.
 */
public class InteractWithAltarLeaf extends LeafTask {
    private ChaoticPrayer bot;
    private GameObject obj;

    public InteractWithAltarLeaf(ChaoticPrayer bot) {
        this.bot = bot;
    }

    public InteractWithAltarLeaf(ChaoticPrayer bot, GameObject obj) {
        this.bot = bot;
        this.obj = obj;
    }

    @Override
    public void execute() {
        if (Inventory.contains(bot.chosenBone)) {
            if (obj == null || !obj.isValid()) {
                obj = GameObjects.newQuery().names("Chaos altar").actions("Pray-at").results().first();
            }

            if (!obj.isVisible()) {
                Camera.turnTo(obj);
            }

            SpriteItemQueryResults items = Inventory.newQuery().names(bot.chosenBone).results();
            if (!items.isEmpty()) {
                boolean isItemSelected = false;
                SpriteItem randomItem = items.random();

                if (randomItem != null && randomItem.isValid() && randomItem.interact("Use")) {
                    isItemSelected = Execution.delayUntil(() -> Inventory.getSelectedItem() != null, 800, 1800);
                }

                if (isItemSelected && obj.click()) {
                    if (Execution.delayUntil(Players.getLocal()::isMoving, 1000, 3000)) {
                        Execution.delayWhile(Players.getLocal()::isMoving);
                    }
                }
            }
        }
    }
}
