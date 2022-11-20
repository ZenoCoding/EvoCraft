package me.zenox.superitems.recipe;

import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

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

        final Recipe PURIFIED_MAGMA_DISTILLATE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE).getItem())
                .id("purified_magma_distillate")
                .shape("TML", "MMM", "LMT")
                .addChoice('M', new RecipeChoice.ExactChoice(ItemRegistry.ENCHANTED_MAGMA_BLOCK.getItemStack(1)))
                .addChoice('L', new RecipeChoice.MaterialChoice(Material.LAVA_BUCKET))
                .addChoice('T', new RecipeChoice.MaterialChoice(Material.GHAST_TEAR))
                .build());

        final Recipe TITANIUM_CUBE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE).getItem())
                .id("purified_magma_distillate")
                .shape("III", "III", "III")
                .addChoice('I', new RecipeChoice.MaterialChoice(Material.IRON_BLOCK))
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

        final Recipe PAGES_OF_AGONY = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.PAGES_OF_AGONY).getItem())
                .id("pages_of_agony")
                .shape("PPP", "PTP", "PPP")
                .addChoice('P', new RecipeChoice.MaterialChoice(Material.PAPER))
                .addChoice('T', new RecipeChoice.ExactChoice(ItemRegistry.TORMENTED_SOUL.getItemStack(1)))
                .build());

        final Recipe DIMENSIONAL_JOURNAL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.DIMENSIONAL_JOURNAL).getItem())
                .id("dimensional_journal")
                .shape("GNE", "PPP", "KKK")
                .addChoice('G', new RecipeChoice.MaterialChoice(Material.GRASS_BLOCK))
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHERRACK))
                .addChoice('E', new RecipeChoice.MaterialChoice(Material.END_STONE))
                .addChoice('P', new RecipeChoice.ExactChoice(ItemRegistry.PAGES_OF_AGONY.getItemStack(1)))
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1))).build());

        final Recipe ENCHANTED_OBSIDIAN = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_OBSIDIAN).getItem())
                .id("enchanted_obsidian")
                .shape("OOO", "OOO", "OOO")
                .addChoice('O', new RecipeChoice.MaterialChoice(Material.OBSIDIAN))
                .build());

        final Recipe COMPACT_OBSIDIAN = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.COMPACT_OBSIDIAN).getItem())
                .id("compact_obsidian")
                .shape("OOO", "OCO", "OOO")
                .addChoice('O', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_OBSIDIAN).getItem()))
                .addChoice('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL).getItem()))
                .build());

        final Recipe CORRUPT_OBSIDIAN = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.CORRUPT_OBSIDIAN).getItem())
                .id("corrupt_obsidian")
                .shape("COO", "ONO", "OOC")
                .addChoice('O', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.COMPACT_OBSIDIAN).getItem()))
                .addChoice('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL).getItem()))
                .addChoice('O', new RecipeChoice.MaterialChoice(Material.NETHER_STAR))
                .build());

        final Recipe CRESTFALLEN_MONOLITH = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.CRESTFALLEN_MONOLITH).getItem())
                .id("crestfallen_monolith")
                .shape("ANO", "C2C", "ONA")
                .addChoice('A', new RecipeChoice.ExactChoice(ItemRegistry.ABSOLUTE_ENDER_PEARL.getItemStack(1)))
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHER_STAR))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .addChoice('2', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_OBSIDIAN.getItemStack(1))).build());

        final Recipe VOID_STONE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_STONE).getItem())
                .id("void_stone")
                .shape("CKC", "CVC", "CCC")
                .addChoice('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL).getItem()))
                .addChoice('K', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.KEVLAR).getItem()))
                .addChoice('V', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ROUGH_VOID_STONE).getItem()))
                .build());

        final Recipe VOID_HELMET = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_HELMET).getItem())
                .id("void_helmet")
                .shape("N2N", "CHC", "   ")
                .addChoice('N', new RecipeChoice.MaterialChoice(Material.NETHER_STAR))
                .addChoice('H', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX).getItem()))
                .addChoice('2', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_OBSIDIAN).getItem()))
                .addChoice('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL).getItem()))
                .build());

        final Recipe VOID_CHESTPLATE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_CHESTPLATE).getItem())
                .id("void_chestplate")
                .shape("K K", "DMD", "HOH")
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1)))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('D', new RecipeChoice.ExactChoice(ItemRegistry.DARK_SKULL.getItemStack(1)))
                .addChoice('H', new RecipeChoice.ExactChoice(ItemRegistry.HYPER_CRUX.getItemStack(1)))
                .addChoice('M', new RecipeChoice.ExactChoice(ItemRegistry.CRESTFALLEN_MONOLITH.getItemStack(1)))
                .build());

        final Recipe VOID_LEGGINGS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_LEGGINGS).getItem())
                .id("void_leggings")
                .shape("OMO", "C C", "K K")
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1)))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('M', new RecipeChoice.ExactChoice(ItemRegistry.CRESTFALLEN_MONOLITH.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .build());

        final Recipe VOID_BOOTS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_BOOTS).getItem())
                .id("void_boots")
                .shape("KDK", "ACA", "OCO")
                .addChoice('K', new RecipeChoice.ExactChoice(ItemRegistry.KEVLAR.getItemStack(1)))
                .addChoice('O', new RecipeChoice.ExactChoice(ItemRegistry.COMPACT_OBSIDIAN.getItemStack(1)))
                .addChoice('D', new RecipeChoice.ExactChoice(ItemRegistry.DARK_SKULL.getItemStack(1)))
                .addChoice('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .addChoice('A', new RecipeChoice.ExactChoice(ItemRegistry.ABSOLUTE_ENDER_PEARL.getItemStack(1)))
                .build());

        final Recipe TITANIUM_BOOTS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_BOOTS).getItem())
                .id("void_boots")
                .shape("KDK", "ACA", "AAA")


                .addChoice('A', new RecipeChoice.ExactChoice(ItemRegistry.TITANIUM_CUBE.getItemStack(1)))
                //if its more than 1 we have to increase stats
                .build());
    }

    private static Recipe registerRecipe(Recipe recipe) {
        registeredRecipes.add(recipe);
        return recipe;
    }

    public static void registerRecipes() {
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + registeredRecipes.size() + ChatColor.WHITE + " recipes.");
    }
}
