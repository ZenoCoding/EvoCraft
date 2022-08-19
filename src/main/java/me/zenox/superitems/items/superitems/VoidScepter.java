package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.MagicMissile;
import me.zenox.superitems.items.abilities.VoidWarp;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class VoidScepter extends ComplexItem {
    public VoidScepter() {
        super("Void Scepter", "void_scepter", Rarity.EPIC, Type.STAFF, Material.NETHERITE_SHOVEL, Map.of(), List.of(new VoidWarp()));

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "Forged from the souls of corrupted endermen and shoots everlasting corruption...");

        this.getMeta().setLore(lore);

    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }

}
