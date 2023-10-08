package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InventoryListener implements Listener {
    private final SuperItems plugin;

    public InventoryListener(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.addAll(Arrays.asList(event.getPlayer().getInventory().getContents()));
        contents.addAll(Arrays.asList(event.getPlayer().getInventory().getArmorContents()));
        updateInventory(contents, event.getPlayer());
    }

//    @EventHandler
//    public void inventoryInteractEvent(InventoryClickEvent event) {
//        ArrayList<ItemStack> contents = new ArrayList<>();
//        contents.addAll(Arrays.asList(event.getWhoClicked().getInventory().getContents()));
//        contents.addAll(Arrays.asList(event.getWhoClicked().getInventory().getArmorContents()));
//        contents.add(event.getCurrentItem());
//        updateInventory(contents, event.getWhoClicked());
//    }

    @EventHandler
    public void inventoryDragItemEvent(InventoryDragEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.addAll(Arrays.asList(event.getWhoClicked().getInventory().getContents()));
        contents.addAll(Arrays.asList(event.getWhoClicked().getInventory().getArmorContents()));
        updateInventory(contents, event.getWhoClicked());
    }

    @EventHandler
    public void inventoryPickupEvent(EntityPickupItemEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getItem().getItemStack());
        updateInventory(contents, event.getEntity());
    }

    private void updateInventory(List<ItemStack> contents, Entity p) {
        contents.removeIf(Objects::isNull);

        // Update ComplexItems
        for (ItemStack item : contents) {
            if (item.getItemMeta() == null) continue;
            ComplexItemStack itemStack = ComplexItemStack.of(item);
            //itemStack.update(false);
        }
    }
}
