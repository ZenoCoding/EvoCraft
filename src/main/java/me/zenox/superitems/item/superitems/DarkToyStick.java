package me.zenox.superitems.item.superitems;

import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.abilities.DarkMissile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class DarkToyStick extends ComplexItem {
    public DarkToyStick() {
        super("dark_toy_stick", Rarity.MYTHIC, Type.SUPERITEM, Material.STICK, Map.of(), List.of(new DarkMissile(10)));

        List<String> lore = List.of(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Pulling strings... behind closed curtains.");

        this.getMeta().setLore(lore);
    }
}
