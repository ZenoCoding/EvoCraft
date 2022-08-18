package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.ShapedRecipeBuilder;
import me.zenox.superitems.util.ShapelessRecipeBuilder;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

import static me.zenox.superitems.items.ItemRegistry.*;

public class RecipeRegistry {

    public static final List<Recipe> registeredRecipes = new ArrayList<>();

    static {


        final Recipe BLAZE_TO_ENCHANTED_BLAZE = registerRecipe(new ShapedRecipeBuilder().setResult(ENCHANTED_BLAZE_ROD.getItemStack(1)).id("blaze_to_enchanted_blaze").shape("BBB", "BBB", "BBB").addChoice('B', new RecipeChoice.MaterialChoice(Material.BLAZE_ROD)).build());

        final Recipe ENCHANTED_BLAZE_TO_BLAZE = registerRecipe(new ShapelessRecipeBuilder().setResult(new ItemStack(Material.BLAZE_ROD, 9)).id("enchanted_blaze_to_blaze").addChoice(new RecipeChoice.ExactChoice(ENCHANTED_BLAZE_ROD.getItemStack(1))).build());


    }

    private static Recipe registerRecipe(Recipe recipe){
        registeredRecipes.add(recipe);
        return recipe;
    }

    public static void registerRecipes(){
        Util.logToConsole(ChatColor.WHITE + "Registering " + registeredRecipes.size() + "recipes.");
    }
}
