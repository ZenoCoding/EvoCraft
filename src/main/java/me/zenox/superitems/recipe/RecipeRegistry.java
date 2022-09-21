package me.zenox.superitems.recipe;

import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeRegistry {

    public static final List<Recipe> registeredRecipes = new ArrayList<>();

    static {


        final Recipe BLAZE_TO_ENCHANTED_BLAZE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD).getItem())
                .id("blaze_to_enchanted_blaze")
                .shape("BBB", "BBB", "BBB")
                .addChoice('B', new MaterialAmountChoice(Material.BLAZE_ROD, 5))
                .build());

        final Recipe ENCHANTED_BLAZE_TO_BLAZE = registerRecipe(new ShapelessRecipeBuilder()
                .setResult(new ItemStack(Material.BLAZE_ROD, 9))
                .id("enchanted_blaze_to_blaze")
                .addChoice(new ComplexChoice(ItemRegistry.ENCHANTED_BLAZE_ROD, -1)).build());

        final Recipe ENCHANTED_ENDER_PEARL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_ENDER_PEARL).getItem())
                .id("enchanted_ender_pearl")
                .shape("EEE", "EEE", "EEE")
                .addChoice('E', new MaterialAmountChoice(Material.ENDER_PEARL, 9))
                .build());

        final Recipe ABSOLUTE_ENDER_PEARL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL).getItem())
                .id("absolute_ender_pearl")
                .shape("EEE", "EEE", "EEE")
                .addChoice('E', new ComplexChoice(ItemRegistry.ENCHANTED_ENDER_PEARL, 9))
                .build());

    }

    private static Recipe registerRecipe(Recipe recipe){
        registeredRecipes.add(recipe);
        return recipe;
    }

    public static void registerRecipes(){
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + registeredRecipes.size() + ChatColor.WHITE + " recipes.");
    }
}
