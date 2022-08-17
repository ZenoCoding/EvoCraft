package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.BURNING_ASHES;
import static me.zenox.superitems.items.ItemRegistry.PURIFIED_MAGMA_DISTILLATE;

public class DarkSkull extends ComplexItem {

    public DarkSkull() {
        super("Dark Skull", "dark_skull", Rarity.RARE, Type.MISC, Material.WITHER_SKELETON_SKULL, Map.of());

        List<String> lore = List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "Smoldering hot.");
        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("MWM", "AWA", "AWA");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(BURNING_ASHES.getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(PURIFIED_MAGMA_DISTILLATE.getItemStack(1)));
        recipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        return List.of(recipe);
    }
}