package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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
        updateInventory(contents);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryInteractEvent(InventoryClickEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getCurrentItem());
        if(!event.isCancelled()) updateInventory(contents);
    }

    @EventHandler
    public void inventoryPickupEvent(EntityPickupItemEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getItem().getItemStack());
        updateInventory(contents);
    }

    @EventHandler
    public void itemDropEvent(EntityDropItemEvent event){
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getItemDrop().getItemStack());
        updateInventory(contents);
    }

    @EventHandler
    public void itemDropEvent(BlockDropItemEvent event){
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.addAll(event.getItems().stream().map(Item::getItemStack).toList());
        updateInventory(contents);
    }

    private void updateInventory(List<ItemStack> contents) {
        contents.removeIf(Objects::isNull);

        // Update ComplexItems
        for (ItemStack item : contents) {
            if (item.getItemMeta() == null) continue;
            ComplexItemStack itemStack = ComplexItemStack.of(item);
            itemStack.updateItem();
        }
    }
}
