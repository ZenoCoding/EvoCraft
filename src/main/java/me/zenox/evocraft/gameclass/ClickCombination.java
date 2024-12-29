package me.zenox.evocraft.gameclass;

import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ClickCombination {
    LEFT_LEFT,
    LEFT_RIGHT,
    RIGHT_LEFT,
    RIGHT_RIGHT;

    public static @Nullable ClickCombination getFromChars(char first, char second) {
        return switch (Character.toLowerCase(first)) {
            case 'l' -> switch (Character.toLowerCase(second)) {
                case 'l' -> LEFT_LEFT;
                case 'r' -> LEFT_RIGHT;
                default -> null;
            };
            case 'r' -> switch (Character.toLowerCase(second)) {
                case 'l' -> RIGHT_LEFT;
                case 'r' -> RIGHT_RIGHT;
                default -> null;
            };
            default -> null;
        };
    }

    private @Nullable ClickCombination detectCombination(@NotNull Action first, Action second) {
        if (first.isLeftClick() && second.isLeftClick()) return ClickCombination.LEFT_LEFT;
        if (first.isLeftClick() && second.isRightClick()) return ClickCombination.LEFT_RIGHT;
        if (first.isRightClick() && second.isLeftClick()) return ClickCombination.RIGHT_LEFT;
        if (first.isRightClick() && second.isRightClick()) return ClickCombination.RIGHT_RIGHT;
        return null;  // Default case, should ideally not be reached
    }
}
