package me.zenox.superitems.recipe;

import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class RecipeRegistry {

    static {


        final ComplexRecipe BLAZE_TO_ENCHANTED_BLAZE = new ShapelessComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD))
                .id("blaze_to_enchanted_blaze")
                .addChoice(new ComplexChoice(Material.BLAZE_ROD, 128))
                .register();

        final ComplexRecipe ENCHANTED_BLAZE_TO_BLAZE = new ShapelessComplexRecipe()
                .setResult(Material.BLAZE_ROD, 9)
                .id("enchanted_blaze_to_blaze")
                .addChoice(new ComplexChoice(ItemRegistry.ENCHANTED_BLAZE_ROD, 1)).register();

        final ComplexRecipe ENCHANTED_ENDER_PEARL = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_ENDER_PEARL))
                .id("enchanted_ender_pearl")
                .shape("EEE", "EEE", "EEE")
                .addChoice('E', new ComplexChoice(Material.ENDER_PEARL, 9))
                .register();

        final ComplexRecipe ABSOLUTE_ENDER_PEARL = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL))
                .id("absolute_ender_pearl")
                .shape("EEE", "EEE", "EEE")
                .addChoice('E', new ComplexChoice(ItemRegistry.ENCHANTED_ENDER_PEARL, 9))
                .register();

        final ComplexRecipe PURIFIED_MAGMA_DISTILLATE = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE))
                .id("purified_magma_distillate")
                .shape("TML", "MMM", "LMT")
                .addChoice('M', new ComplexChoice(ItemRegistry.ENCHANTED_MAGMA_BLOCK))
                .addChoice('L', new ComplexChoice(Material.LAVA_BUCKET))
                .addChoice('T', new ComplexChoice(Material.GHAST_TEAR))
                .register();

        final ComplexRecipe TITANIUM_CUBE = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE))
                .id("purified_magma_distillate")
                .shape("III", "III", "III")
                .addChoice('I', new ComplexChoice(Material.IRON_BLOCK))
                .register();

        final ComplexRecipe MAGIC_TOY_STICK = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.MAGIC_TOY_STICK))
                .id("magic_toy_stick")
                .shape("PHP", "TDT", "TDT")
                .addChoice('P', new ComplexChoice(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE)))
                .addChoice('H', new ComplexChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX)))
                .addChoice('T', new ComplexChoice(new ComplexItemStack(ItemRegistry.TITANIUM_CUBE)))
                .addChoice('P', new ComplexChoice(Material.DEBUG_STICK))
                .register();

        final ComplexRecipe SOUL_CRYSTAL = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.SOUL_CRYSTAL))
                .id("soul_crystal")
                .shape("BEB", "ECE", "BEB")
                .addChoice('B', new ComplexChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD)))
                .addChoice('E', new ComplexChoice(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL)))
                .addChoice('C', new ComplexChoice(Material.END_CRYSTAL))
                .register();

        final ComplexRecipe FIERY_EMBER_STAFF = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.FIERY_EMBER_STAFF))
                .id("fiery_ember_staff")
                .shape("FFF", "APA", "MPM")
                .addChoice('A', new ComplexChoice(new ComplexItemStack(ItemRegistry.BURNING_ASHES)))
                .addChoice('P', new ComplexChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER)))
                .addChoice('M', new ComplexChoice(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE)))
                .addChoice('F', new ComplexChoice(Material.FIRE_CHARGE))
                .register();

        final ComplexRecipe DARK_EMBER_STAFF = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.DARK_EMBER_STAFF))
                .id("dark_ember_staff")
                .shape("RWR", "PSP", "MPM")
                .addChoice('S', new ComplexChoice(new ComplexItemStack(ItemRegistry.FIERY_EMBER_STAFF)))
                .addChoice('P', new ComplexChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER)))
                .addChoice('M', new ComplexChoice(new ComplexItemStack(ItemRegistry.PURIFIED_MAGMA_DISTILLATE)))
                .addChoice('W', new ComplexChoice(new ComplexItemStack(ItemRegistry.DARK_SKULL)))
                .addChoice('R', new ComplexChoice(Material.WITHER_ROSE))
                .register();

        final ComplexRecipe TORMENTED_BLADE = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.TORMENTED_BLADE))
                .id("tormented_blade")
                .shape("ITI", "MBI", " B ")
                .addChoice('B', new ComplexChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_BLAZE_ROD)))
                .addChoice('M', new ComplexChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER)))
                .addChoice('T', new ComplexChoice(new ComplexItemStack(ItemRegistry.TORMENTED_SOUL)))
                .addChoice('I', new ComplexChoice(new ComplexItemStack(ItemRegistry.TITANIUM_CUBE)))
                .register();

        final ComplexRecipe CRUCIFIED_AMULET = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.CRUCIFIED_AMULET))
                .id("crucified_amulet")
                .shape("STS", "AHA", "STS")
                .addChoice('H', new ComplexChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX)))
                .addChoice('A', new ComplexChoice(new ComplexItemStack(ItemRegistry.BURNING_ASHES)))
                .addChoice('T', new ComplexChoice(new ComplexItemStack(ItemRegistry.TORMENTED_SOUL)))
                .addChoice('S', new ComplexChoice(Material.SOUL_SAND))
                .register();

        final ComplexRecipe PSYCHEDELIC_ORB = new ShapelessComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.PSYCHEDELIC_ORB))
                .id("psychedelic_orb")
                .addChoice(new ComplexChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX)))
                .addChoice(new ComplexChoice(new ComplexItemStack(ItemRegistry.ABSOLUTE_ENDER_PEARL)))
                .register();

        final ComplexRecipe TOUGH_FABRIC = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.TOUGH_FABRIC))
                .id("tough_fabric")
                .shape("SSS", "SSS", "SSS")
                .addChoice('S', new ComplexChoice(new ComplexItemStack(ItemRegistry.RAVAGER_SKIN)))
                .register();

        final ComplexRecipe KEVLAR = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.KEVLAR))
                .id("kevlar")
                .shape("SSS", "SSS", "SSS")
                .addChoice('S', new ComplexChoice(new ComplexItemStack(ItemRegistry.TOUGH_FABRIC)))
                .register();

        final ComplexRecipe TOTEM_POLE = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.TOTEM_POLE))
                .id("totem_pole")
                .shape("TCT", "DND", "DND")
                .addChoice('C', new ComplexChoice(new ComplexItemStack(ItemRegistry.CRULEN_SHARD)))
                .addChoice('D', new ComplexChoice(Material.DEBUG_STICK))
                .addChoice('N', new ComplexChoice(Material.NETHER_STAR))
                .addChoice('T', new ComplexChoice(Material.TOTEM_OF_UNDYING))
                .register();

        final ComplexRecipe PAGES_OF_AGONY = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.PAGES_OF_AGONY))
                .id("pages_of_agony")
                .shape("PPP", "PTP", "PPP")
                .addChoice('P', new ComplexChoice(Material.PAPER))
                .addChoice('T', new ComplexChoice(ItemRegistry.TORMENTED_SOUL))
                .register();

        final ComplexRecipe DIMENSIONAL_JOURNAL = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.DIMENSIONAL_JOURNAL))
                .id("dimensional_journal")
                .shape("GNE", "PPP", "KKK")
                .addChoice('G', new ComplexChoice(Material.GRASS_BLOCK))
                .addChoice('N', new ComplexChoice(Material.NETHERRACK))
                .addChoice('E', new ComplexChoice(Material.END_STONE))
                .addChoice('P', new ComplexChoice(ItemRegistry.PAGES_OF_AGONY))
                .addChoice('K', new ComplexChoice(ItemRegistry.KEVLAR)).register();

        final ComplexRecipe ENCHANTED_OBSIDIAN = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.ENCHANTED_OBSIDIAN))
                .id("enchanted_obsidian")
                .shape("OOO", "OOO", "OOO")
                .addChoice('O', new ComplexChoice(Material.OBSIDIAN))
                .register();

        final ComplexRecipe COMPACT_OBSIDIAN = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.COMPACT_OBSIDIAN))
                .id("compact_obsidian")
                .shape("OOO", "OCO", "OOO")
                .addChoice('O', new ComplexChoice(new ComplexItemStack(ItemRegistry.ENCHANTED_OBSIDIAN)))
                .addChoice('C', new ComplexChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL)))
                .register();

        final ComplexRecipe CORRUPT_OBSIDIAN = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.CORRUPT_OBSIDIAN))
                .id("corrupt_obsidian")
                .shape("COO", "ONO", "OOC")
                .addChoice('O', new ComplexChoice(new ComplexItemStack(ItemRegistry.COMPACT_OBSIDIAN)))
                .addChoice('C', new ComplexChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL)))
                .addChoice('O', new ComplexChoice(Material.NETHER_STAR))
                .register();

        final ComplexRecipe CRESTFALLEN_MONOLITH = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.CRESTFALLEN_MONOLITH))
                .id("crestfallen_monolith")
                .shape("ANO", "C2C", "ONA")
                .addChoice('A', new ComplexChoice(ItemRegistry.ABSOLUTE_ENDER_PEARL))
                .addChoice('N', new ComplexChoice(Material.NETHER_STAR))
                .addChoice('O', new ComplexChoice(ItemRegistry.COMPACT_OBSIDIAN))
                .addChoice('C', new ComplexChoice(ItemRegistry.CORRUPT_PEARL))
                .addChoice('2', new ComplexChoice(ItemRegistry.CORRUPT_OBSIDIAN)).register();

        final ComplexRecipe VOID_STONE = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_STONE))
                .id("void_stone")
                .shape("CKC", "CVC", "CCC")
                .addChoice('C', new ComplexChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL)))
                .addChoice('K', new ComplexChoice(new ComplexItemStack(ItemRegistry.KEVLAR)))
                .addChoice('V', new ComplexChoice(new ComplexItemStack(ItemRegistry.ROUGH_VOID_STONE)))
                .register();

        final ComplexRecipe VOID_HELMET = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_HELMET))
                .id("void_helmet")
                .shape("N2N", "CHC", "   ")
                .addChoice('N', new ComplexChoice(Material.NETHER_STAR))
                .addChoice('H', new ComplexChoice(new ComplexItemStack(ItemRegistry.HYPER_CRUX)))
                .addChoice('2', new ComplexChoice(new ComplexItemStack(ItemRegistry.CORRUPT_OBSIDIAN)))
                .addChoice('C', new ComplexChoice(new ComplexItemStack(ItemRegistry.CORRUPT_PEARL)))
                .register();

        final ComplexRecipe VOID_CHESTPLATE = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_CHESTPLATE))
                .id("void_chestplate")
                .shape("K K", "DMD", "HOH")
                .addChoice('K', new ComplexChoice(ItemRegistry.KEVLAR))
                .addChoice('O', new ComplexChoice(ItemRegistry.COMPACT_OBSIDIAN))
                .addChoice('D', new ComplexChoice(ItemRegistry.DARK_SKULL))
                .addChoice('H', new ComplexChoice(ItemRegistry.HYPER_CRUX))
                .addChoice('M', new ComplexChoice(ItemRegistry.CRESTFALLEN_MONOLITH))
                .register();

        final ComplexRecipe VOID_LEGGINGS = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_LEGGINGS))
                .id("void_leggings")
                .shape("OMO", "C C", "K K")
                .addChoice('K', new ComplexChoice(ItemRegistry.KEVLAR))
                .addChoice('O', new ComplexChoice(ItemRegistry.COMPACT_OBSIDIAN))
                .addChoice('M', new ComplexChoice(ItemRegistry.CRESTFALLEN_MONOLITH))
                .addChoice('C', new ComplexChoice(ItemRegistry.CORRUPT_PEARL))
                .register();

        final ComplexRecipe VOID_BOOTS = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_BOOTS))
                .id("void_boots")
                .shape("KDK", "ACA", "OCO")
                .addChoice('K', new ComplexChoice(ItemRegistry.KEVLAR))
                .addChoice('O', new ComplexChoice(ItemRegistry.COMPACT_OBSIDIAN))
                .addChoice('D', new ComplexChoice(ItemRegistry.DARK_SKULL))
                .addChoice('C', new ComplexChoice(ItemRegistry.CORRUPT_PEARL))
                .addChoice('A', new ComplexChoice(ItemRegistry.ABSOLUTE_ENDER_PEARL))
                .register();

        final ComplexRecipe TITANIUM_BOOTS = new ShapedComplexRecipe()
                .setResult(new ComplexItemStack(ItemRegistry.VOID_BOOTS))
                .id("void_boots")
                .shape("KDK", "ACA", "AAA")

                .addChoice('A', new ComplexChoice(ItemRegistry.TITANIUM_CUBE))
                //if it's more than 1 we have to increase stats
                .register();

    }

    public static void registerRecipes() {
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + ComplexRecipe.registeredRecipes.size() + ChatColor.WHITE + " recipes.");
    }
}
