package me.zenox.evocraft.loot;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LootTableEntry {
    private final ItemStack item;
    private final int minAmount;
    private final int maxAmount;
    private final double chance;

    public LootTableEntry(ItemStack item, int minAmount, int maxAmount, double chance) {
        this.item = item;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.chance = chance;
    }

    public ItemStack getItemClone() {
        return item.clone();
    }

    public ItemStack getItemRandomAmount() {
        Random r = new Random();
        ItemStack item = getItemClone();
        int amount;
        if (minAmount == maxAmount) {
            amount = minAmount;
        } else {
            amount = r.nextInt(minAmount, maxAmount);
        }
        item.setAmount(amount);
        return item;
    }

    public double getChance() {
        return chance;
    }
}
