package me.zenox.evocraft.gui.item;

import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ControlItem;
import me.zenox.evocraft.gui.EnchantingGUI;
import me.zenox.evocraft.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Bookshelf Counter Item for EnchantingTable
 */
public class BookshelfItem extends ControlItem<EnchantingGUI> {

    private EnchantingGUI gui;

    @Override
    public ItemProvider getItemProvider(EnchantingGUI gui) {
        this.gui = gui;
        int bookshelfPower = 0;
        for (Block block : Util.getNearbyBlocks(gui.getETable().getLocation(), 5, 5))
            if(block.getType() == Material.BOOKSHELF) bookshelfPower ++;
        bookshelfPower = Math.min(bookshelfPower, 50);
        gui.setBookshelfPower(bookshelfPower);

        return new ItemBuilder(Material.BOOKSHELF).setDisplayName("§dBookshelf Power").setLegacyLore(List.of("", "§dPower: " + bookshelfPower, "", "§7Gain bookshelf power by placing", "§dbookshelves §7around the enchantment table. (Caps at 50)", "", "§e§nClick to update!"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        notifyWindows();
    }
}
