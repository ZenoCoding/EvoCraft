package me.zenox.evocraft.gui;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.AbstractGui;
import xyz.xenondevs.invui.gui.Gui;


/**
 * hhahahh enchant gui code go brr
 */
public class EnchantGuiBuilder extends AbstractGui.AbstractBuilder<EnchantingGUI, EnchantGuiBuilder.Enchanting> {

    private final Player p;
    private final Block block;

    public EnchantGuiBuilder(Player p, Block block) {
        this.p = p;
        this.block = block;
    }

    @Override
    public @NotNull EnchantingGUI build() {
        if (structure == null)
            throw new IllegalStateException("Structure is not defined.");
        EnchantingGUI gui = new EnchantingGUI(structure, p, block);
        gui.setBackground(background);
        return gui;
    }

    interface Enchanting extends Gui.Builder<EnchantingGUI, Enchanting> {}
}

