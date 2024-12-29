package me.zenox.evocraft.gui.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.AbstractGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ControlItem;
import xyz.xenondevs.invui.window.Window;

/**
 * "Empty" item that can be replaced with another item via clicking on it
 */
public class CloseItem extends ControlItem<AbstractGui> {

    private AbstractGui gui;

    @Override
    public ItemProvider getItemProvider(AbstractGui gui) {
        this.gui = gui;
        return new ItemBuilder(Material.BARRIER).setDisplayName("§cClose Menu");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        for (Window window :
                getWindows()) {
            if(window.getCurrentViewer().equals(player)){
                window.close();
            }
        }
    }
}
