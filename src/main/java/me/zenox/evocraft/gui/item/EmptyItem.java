package me.zenox.evocraft.gui.item;

import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * "Empty" item that can be replaced with another item via clicking on it
 */
public class EmptyItem extends AbstractItem {

    private ItemStack item = new ItemStack(Material.AIR);

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(item);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        ItemStack cursorItem = event.getCursor();
        event.setCursor(item);
        item = cursorItem;
        notifyWindows();
    }
}
