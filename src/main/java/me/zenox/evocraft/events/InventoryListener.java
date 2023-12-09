package me.zenox.evocraft.events;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.item.ComplexItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InventoryListener implements Listener {
    private final EvoCraft plugin;

    public InventoryListener(EvoCraft plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.addAll(Arrays.asList(event.getPlayer().getInventory().getContents()));
        contents.addAll(Arrays.asList(event.getPlayer().getInventory().getArmorContents()));
        updateItems(contents);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryInteractEvent(InventoryClickEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getCurrentItem());
        contents.add(event.getCursor());
        if(!event.isCancelled()) updateItems(contents);
    }

    @EventHandler
    public void inventoryPickupEvent(EntityPickupItemEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getItem().getItemStack());
        updateItems(contents);
    }

    @EventHandler
    public void itemDropEvent(EntityDropItemEvent event){
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getItemDrop().getItemStack());
        updateItems(contents);
    }

    @EventHandler
    public void itemDropEvent(BlockDropItemEvent event){
        ArrayList<ItemStack> contents = new ArrayList<>(event.getItems().stream().map(Item::getItemStack).toList());
        updateItems(contents);
    }

    @EventHandler
    public void creativeEvent(InventoryCreativeEvent event) {
        ArrayList<ItemStack> contents = new ArrayList<>();
        contents.add(event.getCursor());
        updateItems(contents);
    }

    private void updateItems(List<ItemStack> contents) {
        contents.removeIf(Objects::isNull);

        // Update ComplexItems
        for (ItemStack item : contents) {
            if (item.getItemMeta() == null) continue;
            ComplexItemStack itemStack = ComplexItemStack.of(item);
            itemStack.updateItem();
        }
    }
}
