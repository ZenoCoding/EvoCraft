package me.zenox.superitems.loot;

import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTable {
    private final String id;
    private List<LootTableEntry> table = new ArrayList();

    public LootTable(String id, List<LootTableEntry> table) {
        this.id = id;
        this.table = table;
    }

    private Inventory getLootGui(List<ItemStack> items) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        Inventory inv = Bukkit.createInventory(null, 9, this.id.replace("_", " ") + " Loot");
        for (ItemStack item : items) inv.addItem(item);
        return inv;
    }

    public List<ItemStack> rollItems(double multiplier) {
        List<ItemStack> items = new ArrayList();
        Random r = new Random();
        for (LootTableEntry entry : table) {
            if (r.nextDouble() <= entry.getChance() * multiplier) items.add(entry.getItemRandomAmount());
        }
        return items;
    }

    public List<ItemStack> rollItems() {
        return rollItems(1);
    }

    public void openLootGUI(Player p, int threatlevel) {
        int multiplier = 1 + threatlevel / 10;
        Util.sendMessage(p, "Threat Level: " + threatlevel);
        p.openInventory(getLootGui(rollItems(multiplier)));
    }

    public String getId() {
        return id;
    }
}

