package me.zenox.evocraft.gui;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.AbstractGui;


/**
 * A builder class for {@link EnchantingGui}.
 */
public class EnchantGuiBuilder extends AbstractGui.AbstractBuilder<EnchantingGui, EnchantingGui.Enchanting> implements EnchantingGui.Enchanting {

    private final Player p;
    private final Block block;

    public EnchantGuiBuilder(Player p, Block block) {
        this.p = p;
        this.block = block;
    }

    @Override
    public @NotNull EnchantingGui build() {
        if (structure == null)
            throw new IllegalStateException("Structure is not defined.");
        EnchantingGui gui = new EnchantingGui(structure, p, block);
        gui.setBackground(background);
        return gui;
    }
}

