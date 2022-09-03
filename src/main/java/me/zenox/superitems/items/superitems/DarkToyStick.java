package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.DarkMissile;
import me.zenox.superitems.items.abilities.MagicMissile;
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
        super("Dark Toy Stick", "dark_toy_stick", Rarity.MYTHIC, Type.SUPERITEM, Material.STICK, Map.of(), List.of(new DarkMissile(10)));

        List<String> lore = List.of(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Pulling strings... behind closed curtains.");

        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        return List.of();
    }
}
