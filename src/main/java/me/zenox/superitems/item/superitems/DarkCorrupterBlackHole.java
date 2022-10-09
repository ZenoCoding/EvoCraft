package me.zenox.superitems.item.superitems;

import me.zenox.superitems.abilities.Corruption;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class DarkCorrupterBlackHole extends ComplexItem {
    public DarkCorrupterBlackHole() {
        super("dark_corrupter_gravity", Rarity.EPIC, Type.STAFF, Material.BLACK_CANDLE, Map.of(), List.of(new Corruption(10, true, Double.MAX_VALUE)));

        List<String> lore = List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "Black hole magic!");

        this.getMeta().setLore(lore);
    }
}
