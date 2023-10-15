package me.zenox.evocraft.gui.item;

import de.studiocode.invui.gui.impl.SimpleGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.ControlItem;
import de.studiocode.invui.window.Window;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * "Empty" item that can be replaced with another item via clicking on it
 */
public class CloseItem extends ControlItem<SimpleGUI> {

    private SimpleGUI gui;

    @Override
    public ItemProvider getItemProvider(SimpleGUI gui) {
        this.gui = gui;
        return new ItemBuilder(Material.BARRIER).setDisplayName("§cClose Menu");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        for (Window window :
                getWindows()) {
            if(window.getCurrentViewer().equals(player)){
                window.closeForViewer();
            }
        }
    }
}
