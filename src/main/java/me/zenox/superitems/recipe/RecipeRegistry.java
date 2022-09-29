package me.zenox.superitems.recipe;

import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

import static me.zenox.superitems.item.ItemRegistry.FIERY_EMBER_STAFF;

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

        final Recipe PURIFIED_MAGMA_DISTILLATE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE).getItem())
                .id("purified_magma_distillate")
                .shape("TML", "MMM", "LMT")
                .addChoice('M', new RecipeChoice.ExactChoice(ItemRegistry.ENCHANTED_MAGMA_BLOCK.getItemStack(1)))
                .addChoice('L', new RecipeChoice.MaterialChoice(Material.LAVA_BUCKET))
                .addChoice('T', new RecipeChoice.MaterialChoice(Material.GHAST_TEAR))
                .build());

        final Recipe MAGIC_TOY_STICK = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.MAGIC_TOY_STICK).getItem())
                .id("magic_toy_stick")
                .shape("PHP", "TDT", "TDT")
                .addChoice('P', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE).getItem()))
                .addChoice('H', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX).getItem()))
                .addChoice('T', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.TITANIUM_CUBE).getItem()))
                .addChoice('P', new RecipeChoice.MaterialChoice(Material.DEBUG_STICK))
                .build());

        final Recipe SOUL_CRYSTAL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.SOUL_CRYSTAL).getItem())
                .id("soul_crystal")
                .shape("BEB", "ECE", "BEB")
                .addChoice('B', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD).getItem()))
                .addChoice('E', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL).getItem()))
                .addChoice('C', new RecipeChoice.MaterialChoice(Material.END_CRYSTAL))
                .build());

        final Recipe FIERY_EMBER_STAFF = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.FIERY_EMBER_STAFF).getItem())
                .id("fiery_ember_staff")
                .shape("FFF", "APA", "MPM")
                .addChoice('A', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.BURNING_ASHES).getItem()))
                .addChoice('P', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER).getItem()))
                .addChoice('M', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE).getItem()))
                .addChoice('F', new RecipeChoice.MaterialChoice(Material.FIRE_CHARGE))
                .build());

        final Recipe DARK_EMBER_STAFF = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.DARK_EMBER_STAFF).getItem())
                .id("dark_ember_staff")
                .shape("RWR", "PSP", "MPM")
                .addChoice('S', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.FIERY_EMBER_STAFF).getItem()))
                .addChoice('P', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER).getItem()))
                .addChoice('M', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE).getItem()))
                .addChoice('W', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.DARK_SKULL).getItem()))
                .addChoice('R', new RecipeChoice.MaterialChoice(Material.WITHER_ROSE))
                .build());

        final Recipe TORMENTED_BLADE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.TORMENTED_BLADE).getItem())
                .id("tormented_blade")
                .shape("ITI", "MBI", " B ")
                .addChoice('B', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD).getItem()))
                .addChoice('M', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER).getItem()))
                .addChoice('T', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.TORMENTED_SOUL).getItem()))
                .addChoice('I', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.TITANIUM_CUBE).getItem()))
                .build());

        final Recipe CRUCIFIED_AMULET = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.CRUCIFIED_AMULET).getItem())
                .id("crucified_amulet")
                .shape("STS", "AHA", "STS")
                .addChoice('H', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX).getItem()))
                .addChoice('A', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.BURNING_ASHES).getItem()))
                .addChoice('T', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.TORMENTED_SOUL).getItem()))
                .addChoice('S', new RecipeChoice.MaterialChoice(Material.SOUL_SAND))
                .build());

        final Recipe PSYCHEDELIC_ORB = registerRecipe(new ShapelessRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.PSYCHEDELIC_ORB).getItem())
                .id("psychedelic_orb")
                .addChoice(new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX).getItem()))
                .addChoice(new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL).getItem()))
                .build());

        final Recipe TOUGH_FABRIC = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.TOUGH_FABRIC).getItem())
                .id("tough_fabric")
                .shape("SSS", "SSS", "SSS")
                .addChoice('S', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.RAVAGER_SKIN).getItem()))
                .build());

        final Recipe KEVLAR = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.KEVLAR).getItem())
                .id("kevlar")
                .shape("SSS", "SSS", "SSS")
                .addChoice('S', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.TOUGH_FABRIC).getItem()))
                .build());

        final Recipe TOTEM_POLE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.TOTEM_POLE).getItem())
                .id("totem_pole")
                .shape("TCT", "DND", "DND")
                .addChoice('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CRULEN_SHARD).getItem()))
                .addChoice('D', new RecipeChoice.MaterialChoice(Material.DEBUG_STICK))
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHER_STAR))
                .addChoice('T', new RecipeChoice.MaterialChoice(Material.TOTEM_OF_UNDYING))
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
