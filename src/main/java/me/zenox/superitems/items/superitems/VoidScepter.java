package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.MagicMissile;
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
        super("Void Scepter", "void_scepter", Rarity.EPIC, Type.STAFF, Material.DIAMOND_SHOVEL, Map.of(), List.of(new MagicMissile(6, true)));

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "Forged from the souls of corrupted endermen...");

        this.getMeta().setLore(lore);

    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
