package me.zenox.evocraft.recipe;

import me.zenox.evocraft.item.ComplexItemStack;
import me.zenox.evocraft.item.ItemRegistry;
import me.zenox.evocraft.item.VanillaItem;
import me.zenox.evocraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeRegistry {

    public static final List<Recipe> registeredRecipes = new ArrayList<>();

    static {


        final Recipe BLAZE_TO_ENCHANTED_BLAZE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD).getItem())
                .id("blaze_to_enchanted_blaze")
                .shape("BBB", "BBB", "BBB")
                .addChoice('B', new RecipeChoice.MaterialChoice(Material.BLAZE_ROD))
                .build());

        final Recipe ENCHANTED_BLAZE_TO_BLAZE = registerRecipe(new ShapelessRecipeBuilder()
                .setResult(new ItemStack(Material.BLAZE_ROD, 9))
                .id("enchanted_blaze_to_blaze")
                .addChoice(new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD).getItem())).build());

        final Recipe ENCHANTED_ENDER_PEARL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_ENDER_PEARL).getItem())
                .id("enchanted_ender_pearl")
                .shape("EEE", "EEE", "EEE")
                .addChoice('E', new RecipeChoice.MaterialChoice(Material.ENDER_PEARL))
                .build());

        final Recipe ENCHANTED_MAGMA_BLOCK = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_MAGMA_BLOCK).getItem())
                .id("enchanted_magma_block")
                .shape("MMM", "MMM", "MMM")
                .addChoice('M', new RecipeChoice.MaterialChoice(Material.MAGMA_BLOCK))
                .build());

        final Recipe ABSOLUTE_ENDER_PEARL = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL).getItem())
                .id("absolute_ender_pearl")
                .shape("EEE", "EEE", "EEE")
                .addChoice('E', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_ENDER_PEARL).getItem()))
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
                .setResult(new ComplexItemStack(ItemRegistry.TITANIUM_CUBE).getItem())
                .id("titanium_cube")
                .shape("III", "III", "III")
                .addChoice('I', new RecipeChoice.MaterialChoice(Material.IRON_BLOCK))
                .build());

        final Recipe MAGIC_TOY_STICK = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.MAGIC_TOY_STICK).getItem())
                .id("magic_toy_stick")
                .shape("EAE", "TDT", "TDT")
                .addChoice('E', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_ENDER_PEARL).getItem()))
                .addChoice('A', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL).getItem()))
                .addChoice('T', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.TITANIUM_CUBE).getItem()))
                .addChoice('D', new RecipeChoice.MaterialChoice(Material.DEBUG_STICK))
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


        final Recipe DIAMANTINE_HELMET = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.DIAMANTINE_HELMET).getItem())
                .id("diamantine_helmet")
                .shape("DDD", "D D", "   ")
                .addChoice('D', new RecipeChoice.ExactChoice(new ComplexItemStack(VanillaItem.of(Material.DIAMOND_BLOCK)).getItem()))
                .build());

        final Recipe DIAMANTINE_CHESTPLATE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.DIAMANTINE_CHESTPLATE).getItem())
                .id("diamantine_chestplate")
                .shape("D D", "DDD", "DDD")
                .addChoice('D', new RecipeChoice.ExactChoice(new ComplexItemStack(VanillaItem.of(Material.DIAMOND_BLOCK)).getItem()))
                .build());

        final Recipe DIAMANTINE_LEGGINGS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.DIAMANTINE_LEGGINGS).getItem())
                .id("diamantine_leggings")
                .shape("DDD", "D D", "D D")
                .addChoice('D', new RecipeChoice.ExactChoice(new ComplexItemStack(VanillaItem.of(Material.DIAMOND_BLOCK)).getItem()))
                .build());

        final Recipe DIAMANTINE_BOOTS = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.DIAMANTINE_BOOTS).getItem())
                .id("diamantine_boots")
                .shape("   ", "D D", "D D")
                .addChoice('D', new RecipeChoice.ExactChoice(new ComplexItemStack(VanillaItem.of(Material.DIAMOND_BLOCK)).getItem()))
                .build());

        final Recipe SHADOW_BLADE = registerRecipe(new ShapedRecipeBuilder()
                .setResult(new ComplexItemStack(ItemRegistry.SHADOW_BLADE).getItem())
                .id("shadow_blade")
                .shape("CCC", "CSC", "CCC")
                .addChoice('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL).getItem()))
                .addChoice('S', new RecipeChoice.ExactChoice(new ComplexItemStack(VanillaItem.of(Material.NETHERITE_SWORD)).getItem()))
                .build());


    }

    private static Recipe registerRecipe(Recipe recipe) {
        registeredRecipes.add(recipe);
        return recipe;
    }

    public static void registerRecipes() {
        for (Recipe recipe : registeredRecipes) {
            try {
                Bukkit.addRecipe(recipe);
            } catch (IllegalStateException e) {
                if (recipe instanceof Keyed) {
                    Bukkit.removeRecipe(((Keyed) recipe).getKey());
                    Bukkit.addRecipe(recipe);
                }
            }
        }
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + registeredRecipes.size() + ChatColor.WHITE + " recipes.");
    }
}
