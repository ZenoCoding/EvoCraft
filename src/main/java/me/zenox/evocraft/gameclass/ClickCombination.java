package me.zenox.evocraft.gameclass;

import org.bukkit.event.block.Action;

public enum ClickCombination {
    LEFT_LEFT,
    LEFT_RIGHT,
    RIGHT_LEFT,
    RIGHT_RIGHT;

    private ClickCombination detectCombination(Action first, Action second) {
        if (first.isLeftClick() && second.isLeftClick()) return ClickCombination.LEFT_LEFT;
        if (first.isLeftClick() && second.isRightClick()) return ClickCombination.LEFT_RIGHT;
        if (first.isRightClick() && second.isLeftClick()) return ClickCombination.RIGHT_LEFT;
        if (first.isRightClick() && second.isRightClick()) return ClickCombination.RIGHT_RIGHT;
        return null;  // Default case, should ideally not be reached
    }
}
