package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.Corruption;
import me.zenox.superitems.items.abilities.CorruptionVisual;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class CorrupterVisual extends ComplexItem {
    public CorrupterVisual() {
        super(ChatColor.DARK_PURPLE + "Dark Corrupter (Visual)", "dark_corrupter_visual", Rarity.EPIC, Type.STAFF, Material.WITHER_SKELETON_SKULL, Map.of(), List.of(new CorruptionVisual(7)));

        List<String> lore = List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "Really fricking dark magic.");

        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
