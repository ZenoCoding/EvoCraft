package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.TOUGH_FABRIC;

public class Kevlar extends ComplexItem {

    public Kevlar() {
        super("kevlar", Rarity.RARE, Type.MISC, Material.LEATHER, Map.of());
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.getMeta().setLore(List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "Bulletproof."));
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("SSS", "SSS", "SSS");
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(TOUGH_FABRIC.getItemStack(1)));
        return List.of(recipe);
    }
}
