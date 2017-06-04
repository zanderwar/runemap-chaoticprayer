package com.johnwaltz.bots.chaoticprayer.leafs;

import com.johnwaltz.bots.chaoticprayer.ChaoticPrayer;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.entities.SummonedFamiliar;
import com.runemate.game.api.rs3.local.hud.interfaces.BeastOfBurden;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import jdk.nashorn.internal.runtime.regexp.RegExp;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by reece on 4/06/2017.
 */
public class FamiliarPreparation extends LeafTask {
    private ChaoticPrayer bot;

    public FamiliarPreparation(ChaoticPrayer bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        SummonedFamiliar familiar = Players.getLocal().getFamiliar();

        if (familiar != null) {
            // We do have a familiar, but is it the right one and will it survive long enough for another round of bones?
            // todo 3 minutes remaining may have to be tweaked depending on time to offer all bones
            if (!Objects.equals(familiar.getName(), bot.chosenFamiliar) || Summoning.getMinutesRemaining() < 3) {
                if (Bank.depositInventoryOfBeastOfBurden()) {
                    bot.currentTask = "Depositing beast of burden items";
                    Execution.delayUntil(() -> BeastOfBurden.getItems().isEmpty());
                }

                if (Bank.close()) {
                    bot.currentTask = "Closing bank to dismiss incorrect or expiring familiar";
                    Execution.delayUntil(() -> (!Bank.isOpen()), 1500, 5000);
                }

                if (familiar.isValid()) {
                    if (familiar.interact("Dismiss")) {
                        ChatDialog.Option option = ChatDialog.getOption("Yes");

                        if (option == null) {
                            if (Execution.delayUntil(() -> (ChatDialog.getOption("Yes") != null), 1500, 3000)) {
                                ChatDialog.getOption("Yes").select();
                                Execution.delayUntil(() -> (Players.getLocal().getFamiliar() == null));
                            }
                        }
                    }

                    LocatableEntityQueryResults<GameObject> simonResults = GameObjects.newQuery().names("Simon").actions("Bank").results();
                    if (!simonResults.isEmpty()) {
                        GameObject simon = simonResults.nearest();

                        if (simon != null) {
                            if (simon.interact("Bank")) {
                                Execution.delayUntil(() -> (Bank.isOpen()), 1500, 5000);
                            }
                        }
                    }
                }
            }
        }

        if (familiar == null && Bank.isOpen()) {
            if (Bank.contains(getPouchName())) {
                SpriteItem pouch = Bank.newQuery().names(getPouchName()).results().first();
                boolean hasPouch = false;

                if (pouch != null && pouch.isValid() && Bank.withdraw(pouch, 1)) {
                    if (Execution.delayUntil(() -> Inventory.contains(getPouchName()), 1500,3000)) {
                        hasPouch = true;
                    }
                }

                boolean hasPoints = true;
                if (hasPouch && Summoning.getPoints() < 13) {
                    SpriteItem potion = null;
                    if ((potion = getAvailableSummoningPotion()) != null) {
                        if (Bank.withdraw(potion, 1)) {
                            if (Execution.delayUntil(() -> Inventory.contains(Pattern.compile("Summoning.+")), 1500, 3000)) {
                                if (Bank.close()) {
                                    Execution.delayUntil(() -> !Bank.isOpen());
                                    SpriteItemQueryResults results = Inventory.newQuery().names(Pattern.compile("Summoning.+")).actions("Drink").results();
                                    SpriteItem invItem = (!results.isEmpty()) ? results.first() : null;
                                    if (invItem != null && invItem.isValid()) {
                                        if (invItem.click()) {
                                            if (Execution.delayUntil(() -> Summoning.getPoints() > 13, 1500, 5000)) {
                                                hasPoints = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (hasPoints) {
                    // find pouch in inventory
                    SpriteItemQueryResults pouchResults = Inventory.newQuery().names(getPouchName()).results();
                    if (!pouchResults.isEmpty()) {
                        pouch = pouchResults.first();

                        if (pouch != null && pouch.isValid()) {
                            if (pouch.click()) {
                                Execution.delayUntil(() -> Players.getLocal().getFamiliar() != null, 1500, 5000);
                            }
                        }
                    }
                }
            }
        }
    }

    public String getPouchName() {
        return bot.chosenFamiliar + " pouch";
    }

    public String getPouchName(String familiar) {
        return familiar + " pouch";
    }

    public SpriteItem getAvailableSummoningPotion() {
        if (!Bank.isOpen()) {
            return null;
        }

        SpriteItemQueryResults results = null;
        String pattern = "Summoning potion .+";
        String pattern2 = "Summoning flask .+";
        if (Bank.contains(Pattern.compile(pattern))) {
            results = Bank.newQuery().names(Pattern.compile(pattern)).results();

            if (!results.isEmpty()) {
                return results.random();
            }
        } else if (Bank.contains(Pattern.compile(pattern2))) {
            results = Bank.newQuery().names(Pattern.compile(pattern2)).results();

            if (!results.isEmpty()) {
                return results.random();
            }
        }

        return null;
    }
}
