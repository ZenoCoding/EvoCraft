package me.zenox.superitems.items;

import me.zenox.superitems.util.ShapedRecipeBuilder;
import me.zenox.superitems.util.ShapelessRecipeBuilder;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeRegistry {

    public static final List<Recipe> registeredRecipes = new ArrayList<>();

    static {


        final Recipe BLAZE_TO_ENCHANTED_BLAZE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.ENCHANTED_BLAZE_ROD.getItemStack(1))
                .id("blaze_to_enchanted_blaze")
                .shape("BBB", "BBB", "BBB")
                .addChoice('B', new RecipeChoice.MaterialChoice(Material.BLAZE_ROD))
                .build());

        final Recipe ENCHANTED_BLAZE_TO_BLAZE = registerRecipe(new ShapelessRecipeBuilder()
                .setResult(new ItemStack(Material.BLAZE_ROD, 9))
                .id("enchanted_blaze_to_blaze")
                .addChoice(new RecipeChoice.ExactChoice(ItemRegistry.ENCHANTED_BLAZE_ROD.getItemStack(1)))
                .build());

        final Recipe MAGMA_TO_ENCH_MAGMA = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.ENCHANTED_MAGMA_BLOCK.getItemStack(1)).id("magma_to_enchanted_magma")
                .shape("MMM", "MMM", "MMM")
                .addChoice('M', new RecipeChoice.MaterialChoice(Material.MAGMA_BLOCK))
                .build());

        final Recipe PAGES_OF_AGONY = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.PAGES_OF_AGONY.getItemStack(1))
                .id("pages_of_agony")
                .shape("PPP", "PTP", "PPP")
                .addChoice('P', new RecipeChoice.MaterialChoice(Material.PAPER))
                .addChoice('T', new RecipeChoice.ExactChoice(ItemRegistry.TORMENTED_SOUL.getItemStack(1)))
                .build());

        final Recipe DIMENSIONAL_JOURNAL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.DIMENSIONAL_JOURNAL.getItemStack(1))
                .id("dimensional_journal")
                .shape("GNE", "PPP", "KKK")
                .addChoice('G', new RecipeChoice.MaterialChoice(Material.GRASS_BLOCK))
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHERRACK))
                .addChoice('E', new RecipeChoice.MaterialChoice(Material.END_STONE))
                .addChoice('P', new RecipeChoice.ExactChoice(ItemRegistry.PAGES_OF_AGONY.getItemStack(1)))
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1))).build());

        final Recipe CRESTFALLEN_MONOLITH = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.CRESTFALLEN_MONOLITH.getItemStack(1))
                .id("crestfallen_monolith")
                .shape("ANO", "C2C", "ONA")
                .addChoice('A', new RecipeChoice.ExactChoice(ItemRegistry.ABSOLUTE_ENDER_PEARL.getItemStack(1)))
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHER_STAR))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .addChoice('2', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_OBSIDIAN.getItemStack(1))).build());

        final Recipe VOID_HELMET = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.VOID_HELMET.getItemStack(1))
                .id("void_helmet")
                .shape("N2N", "CHC", "   ")
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHER_STAR))
                .addChoice('H', new RecipeChoice.ExactChoice(ItemRegistry.HYPER_CRUX.getItemStack(1)))
                .addChoice('2', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_OBSIDIAN.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .build());

        final Recipe VOID_CHESTPLATE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.VOID_CHESTPLATE.getItemStack(1))
                .id("void_chestplate")
                .shape("K K", "DMD", "HOH")
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1)))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('D', new RecipeChoice.ExactChoice(ItemRegistry.DARK_SKULL.getItemStack(1)))
                .addChoice('H', new RecipeChoice.ExactChoice(ItemRegistry.HYPER_CRUX.getItemStack(1)))
                .addChoice('M', new RecipeChoice.ExactChoice(ItemRegistry.CRESTFALLEN_MONOLITH.getItemStack(1)))
                .build());

        final Recipe VOID_LEGGINGS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.VOID_LEGGINGS.getItemStack(1))
                .id("void_leggings")
                .shape("OMO", "C C", "K K")
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1)))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('M', new RecipeChoice.ExactChoice(ItemRegistry.CRESTFALLEN_MONOLITH.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .build());

        final Recipe VOID_BOOTS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(ItemRegistry.VOID_BOOTS.getItemStack(1))
                .id("void_boots")
                .shape("KDK", "ACA", "OCO")
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1)))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('D', new RecipeChoice.ExactChoice(ItemRegistry.DARK_SKULL.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .addChoice('A', new RecipeChoice.ExactChoice(ItemRegistry.ABSOLUTE_ENDER_PEARL.getItemStack(1)))
                .build());


    }

    private static Recipe registerRecipe(Recipe recipe){
        registeredRecipes.add(recipe);
        try {
            Bukkit.addRecipe(recipe);
        } catch (IllegalStateException e) {
            if (recipe instanceof Keyed) {
                //Util.logToConsole("Found duplicate recipe, re-adding.");
                Bukkit.removeRecipe(((Keyed) recipe).getKey());
                Bukkit.addRecipe(recipe);
            } else { /**Util.logToConsole("Found duplicate recipe that wasn't keyed, skipping.");**/}

        }
        return recipe;
    }

    public static void registerRecipes(){
        Util.logToConsole(ChatColor.WHITE + "Registering " + registeredRecipes.size() + " recipes.");
    }
}
