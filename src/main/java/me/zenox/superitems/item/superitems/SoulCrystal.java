package me.zenox.superitems.item.superitems;

import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.SoulRift;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.ABSOLUTE_ENDER_PEARL;
import static me.zenox.superitems.item.ItemRegistry.ENCHANTED_BLAZE_ROD;

public class SoulCrystal extends ComplexItem {
    public SoulCrystal() {
        super("soul_crystal", Rarity.LEGENDARY, Type.DEPLOYABLE, Material.END_CRYSTAL, Map.of(), List.of(new SoulRift()));

    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));

        recipe.shape("BEB", "ECE", "BEB");
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(ENCHANTED_BLAZE_ROD.getItemStack(1)));
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(ABSOLUTE_ENDER_PEARL.getItemStack(1)));
        recipe.setIngredient('C', Material.END_CRYSTAL);
        return List.of(recipe);
    }
}
