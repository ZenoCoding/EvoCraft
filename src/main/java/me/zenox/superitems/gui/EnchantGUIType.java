package me.zenox.superitems.gui;

import de.studiocode.invui.gui.builder.GUIContext;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.impl.SimpleGUI;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

class EnchantGUIType implements GUIType<EnchantingGUI> {

    public EnchantingGUI createGUI(GUIContext context, Player p, Block block) {
        EnchantingGUI gui = new EnchantingGUI(context.getStructure(), p, block);
        gui.setBackground(context.getBackground());
        return gui;
    }

    @Override
    public EnchantingGUI createGUI(GUIContext context) {
        throw new IllegalStateException("You cannot use EnchantGUIType with the normal GUI builder.");
    }

    @Override
    public boolean acceptsGUIs() {
        return false;
    }

    @Override
    public boolean acceptsItems() {
        return false;
    }

    @Override
    public boolean acceptsInventory() {
        return false;
    }

}

