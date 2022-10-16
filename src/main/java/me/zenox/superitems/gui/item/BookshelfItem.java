package me.zenox.superitems.gui.item;

import de.studiocode.invui.gui.impl.SimpleGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.ControlItem;
import de.studiocode.invui.window.Window;
import me.zenox.superitems.gui.EnchantingGUI;
import me.zenox.superitems.util.Util;
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
        for (Block block : Util.getNearbyBlocks(gui.getEtable().getLocation(), 5, 5))
            if(block.getType() == Material.BOOKSHELF) bookshelfPower ++;
        bookshelfPower = Math.min(bookshelfPower, 15);
        gui.setBookshelfPower(bookshelfPower);

        return new ItemBuilder(Material.BOOKSHELF).setDisplayName("§dBookshelf Power").setLegacyLore(List.of("", "§dPower: " + bookshelfPower, "", "§7Gain bookshelf power by placing", "§dbookshelves §around the enchantment table.", "", "§e§nClick to update!"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        notifyWindows();
    }
}
