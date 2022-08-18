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
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(3));

        return List.of(recipe);
        recipe.shape("DGD", "FSF", "TST");
        recipe.setIngredient('D', Material.END_STONE);
        recipe.setIngredient('G', Material.GLOWSTONE_DUST);
        recipe.setIngredient('F', Material.LEGACY_ENDER_PEARL);
        recipe.setIngredient('S', Material.DEBUG_STICK);
        recipe.setIngredient('T', Material.LEGACY_ENDER_PEARL);
    }
}
