package com.johnwaltz.bots.chaoticprayer.branches;

import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

/**
 * Created by reece on 3/06/2017.
 */
public class IsPlayerNearby extends BranchTask {
    @Override
    public TreeTask failureTask() {
        return null;
    }

    @Override
    public TreeTask successTask() {
        return null;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
