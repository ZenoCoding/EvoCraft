package me.zenox.evocraft.item;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.evocraft.abilities.*;
import me.zenox.evocraft.attribute.AttributeRegistry;
import me.zenox.evocraft.gui.EnchantingGUI;
import me.zenox.evocraft.item.basicitems.CorruptPearl;
import me.zenox.evocraft.item.basicitems.GardenerSapling;
import me.zenox.evocraft.item.basicitems.RavagerSkin;
import me.zenox.evocraft.item.basicitems.TormentedSoul;
import me.zenox.evocraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemRegistry {


    public static final ComplexItem GARDENER_SAPLING = new GardenerSapling();
    public static final ComplexItem ENCHANTED_MAGMA_BLOCK = new ComplexItem(new ItemSettings()
            .id("enchanted_magma_block")
            .material(Material.MAGMA_BLOCK));
    public static final ComplexItem PURIFIED_MAGMA_DISTILLATE = new ComplexItem(new ItemSettings()
            .id("purified_magma_distillate")
            .material(Material.MAGMA_CREAM)
            .glow());
    public static final ComplexItem ENCHANTED_BLAZE_ROD = new ComplexItem(new ItemSettings()
            .id("enchanted_blaze_rod")
            .material(Material.BLAZE_ROD)
            .glow());
    public static final ComplexItem BURNING_ASHES = new ComplexItem(new ItemSettings()
            .id("burning_ashes")
            .material(Material.GUNPOWDER)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .glow());
    public static final ComplexItem MOLTEN_POWDER = new ComplexItem(new ItemSettings()
            .id("molten_powder")
            .material(Material.BLAZE_POWDER)
            .glow());
    public static final ComplexItem ENCHANTED_ENDER_PEARL = new ComplexItem(new ItemSettings()
            .id("enchanted_ender_pearl")
            .material(Material.ENDER_PEARL));
    public static final ComplexItem ABSOLUTE_ENDER_PEARL = new ComplexItem(new ItemSettings()
            .id("absolute_ender_pearl")
            .material(Material.ENDER_PEARL)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .glow());
    public static final ComplexItem DARK_SKULL = new ComplexItem(new ItemSettings()
            .id("dark_skull")
            .material(Material.WITHER_SKELETON_SKULL)
            .rarity(ComplexItem.Rarity.UNCOMMON));
    // Cannot be migrated due to custom drop stuff!
    public static final ComplexItem TORMENTED_SOUL = new TormentedSoul();

    public static final ComplexItem TITANIUM_CUBE = new ComplexItem(new ItemSettings()
            .id("titanium_cube")
            .material(Material.IRON_BLOCK)
            .rarity(ComplexItem.Rarity.COMMON));

    public static final ComplexItem MAGIC_TOY_STICK = new ComplexItem(new ItemSettings()
            .id("magic_toy_stick")
            .material(Material.STICK)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.WAND)
            .ability(AbilityRegistry.MAGIC_MISSILE_COMBUST_6));

    public static final ComplexItem SOUL_CRYSTAL = new ComplexItem(new ItemSettings()
            .id("soul_crystal")
            .material(Material.END_CRYSTAL)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.DEPLOYABLE)
            .ability(AbilityRegistry.SOUL_RIFT));

    public static final ComplexItem FIERY_EMBER_STAFF = new ComplexItem(new ItemSettings()
            .id("fiery_ember_staff")
            .material(Material.BLAZE_ROD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.STAFF)
            .modifier(AttributeRegistry.WISDOM, 50)
            .modifier(AttributeRegistry.STRENGTH, 10)
            .ability(AbilityRegistry.SMALL_EMBER_SHOOT));

    public static final ComplexItem DARK_EMBER_STAFF = new ComplexItem(new ItemSettings()
            .id("dark_ember_staff")
            .material(Material.BLAZE_ROD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.STAFF)
            .ability(new EmberAttune())
            .ability(AbilityRegistry.EMBER_SHOOT)
            .modifier(AttributeRegistry.WISDOM, 75)
            .modifier(AttributeRegistry.STRENGTH, 25)
            .variable(EmberAttune.ATTUNEMENT_VARIABLE_TYPE, EmberAttune.Attunement.BLAZEBORN));

    public static final ComplexItem TORMENTED_BLADE = new ComplexItem(new ItemSettings()
            .id("tormented_blade")
            .material(Material.IRON_AXE)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.AXE)
            .modifier(AttributeRegistry.STRENGTH, 25)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 14)
            .modifier(AttributeRegistry.ATTACK_SPEED, -3.2)
            .ability(AbilityRegistry.TARHELM));

    public static final ComplexItem SWORD_OF_JUSTICE = new ComplexItem(new ItemSettings()
            .id("sword_of_justice")
            .material(Material.IRON_SWORD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.SWORD)
            .enchant(Enchantment.DAMAGE_ALL, 10)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 10)
            .modifier(AttributeRegistry.ATTACK_SPEED, 1000)
            .ability(AbilityRegistry.JUSTICE));

    public static final ComplexItem CRUCIFIED_AMULET = new ComplexItem(new ItemSettings()
            .id("crucified_amulet")
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWUwZGRmNDEyYzYwYTdiZWY4NGE3YjhlNmZjYTcxNGQ0ODgyYWYxMTE4ZTk3NDAwYzg4ZDExYmE1YTk0N2RjYSJ9fX0=")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .ability(new Crucify()));

    public static final ComplexItem DESECRATOR_SCALE = new ComplexItem(new ItemSettings()
            .id("desecrator_scale")
            .material(Material.PHANTOM_MEMBRANE));
    public static final ComplexItem DESECRATOR_CLAW = new ComplexItem(new ItemSettings()
            .id("desecrator_claw")
            .material(Material.DAMAGED_ANVIL));

    public static final ComplexItem DESECRATOR_TOE = new ComplexItem(new ItemSettings()
            .id("desecrator_toe")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY1OGU3OTZmYTcxOTRjZjk1MTJkNDEwNjYxNWNmNDQ1MmUwYWNjNTM4OGVmMDA0ZTAxMDEzMjM0Y2Y5ZDg4In19fQ=="));

    public static final ComplexItem CRULEN_SHARD = new ComplexItem(new ItemSettings()
            .id("crulen_shard")
            .material(Material.QUARTZ)
            .glow());

    public static final ComplexItem PYTHEMION_GEM = new ComplexItem(new ItemSettings()
            .id("pythemion_gem")
            .material(Material.EMERALD)
            .glow());

    public static final ComplexItem PSYCHEDELIC_ORB = new ComplexItem(new ItemSettings()
            .id("psychedelic_orb")
            .material(Material.ENDER_PEARL)
            .stat(Stats.WISDOM, 1000d)
            .ability(new Psychic())
            .glow());

    public static final ComplexItem HYPER_CRUX = new ComplexItem(new ItemSettings()
            .id("hyper_crux")
            .material(Material.PLAYER_HEAD)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg4MWM4NzY0ZmJmOTNiMjMxNWNhMTEzNTczZmE3OTlmNGNmZGMwY2E4NjhjODM0MDUyNTVhN2Q1NTZhNzM5ZiJ9fX0=")
            .glow());

    // Event stuff so no migration
    public static final ComplexItem RAVAGER_SKIN = new RavagerSkin();

    public static final ComplexItem TOUGH_FABRIC = new ComplexItem(new ItemSettings()
            .id("tough_fabric")
            .material(Material.LEATHER)
            .glow());

    public static final ComplexItem KEVLAR = new ComplexItem(new ItemSettings()
            .id("kevlar")
            .material(Material.LEATHER)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .glow());

    public static final ComplexItem TOTEM_POLE = new ComplexItem(new ItemSettings()
            .id("totem_pole")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4MDkxMjhhMGU1YTQ0YzJlMzk1MzJlNmJiYzY4MjUyY2I4YzlkNWVjZDI0NmU1OTY1MDc3YzE0N2M3OTVlNyJ9fX0=")
            .ability(AbilityRegistry.CENTRALIZE));

    public static final ComplexItem CORRUPT_TOTEM_POLE = new ComplexItem(new ItemSettings()
            .id("corrupt_totem_pole")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.RARE)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk3YzI4NmI2ZDE2MjM5YTcxZmYxNjc0OTQ0MTZhZDk0MDcxNzIwNTEwY2Y4YTgyYWIxZjQ1MWZmNGE5MDkxNiJ9fX0=")
            .ability(AbilityRegistry.CENTRALIZE_CORRUPT));

    public static final ComplexItem WARPED_CUBE = new ComplexItem(new ItemSettings()
            .id("warped_cube")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVjYjVkYTFjMmVlZDQzYmY2ODUxODllMDgwMjlmYzJhZWVlZGZhZTFjNmEyMTRlNzBmNzRiOGEzMjExYjBhIn19fQ=="));

    public static final ComplexItem ROUGH_VOID_STONE = new ComplexItem(new ItemSettings()
            .id("rough_void_stone")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ0ODc1MGJiNWFkYTI5ZGZmNjgyMGRiNjkzM2RjNjJhMGJmNmJkZTcyNzM0MWViN2RkMTg0NTNhMTBkNjQ5MyJ9fX0="));

    public static final ComplexItem VOID_STONE = new ComplexItem(new ItemSettings()
            .id("void_stone")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ0ODc1MGJiNWFkYTI5ZGZmNjgyMGRiNjkzM2RjNjJhMGJmNmJkZTcyNzM0MWViN2RkMTg0NTNhMTBkNjQ5MyJ9fX0="));

    public static final ComplexItem OBSIDIAN_TABLET = new ComplexItem(new ItemSettings()
            .id("obsidian_tablet")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWExNTlkOWFjMmZkYjE2NDg1OGI2MzUwZTAzYzE5MjRmMTdlNWFhODYxMWEzNDdkNTViNmI4OTgyMGZhZjA5NCJ9fX0="));


    public static final ComplexItem VOID_SCEPTER = new ComplexItem(new ItemSettings()
            .id("void_scepter")
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 15)
            .modifier(AttributeRegistry.ATTACK_SPEED, -3)
            .modifier(AttributeRegistry.WISDOM, 50)
            .abilities(AbilityRegistry.VOID_WARP, AbilityRegistry.VOIDULAR_RECALL)
            .rarity(ComplexItem.Rarity.EPIC)
            .material(Material.NETHERITE_SHOVEL)
            .type(ComplexItem.Type.STAFF)

    );
    public static final ComplexItem OBSIDILITH_SCYTHE = new ComplexItem(new ItemSettings()
            .id("obsidilith_scythe")
            .material(Material.NETHERITE_HOE)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .ability(AbilityRegistry.OBSIDIAN_SHARD)
            .type(ComplexItem.Type.SWORD)
            .modifier(AttributeRegistry.ATTACK_SPEED, -2)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 10)
            .modifier(AttributeRegistry.STRENGTH, 30)
            .modifier(AttributeRegistry.WISDOM, 100));


    public static final ComplexItem CORRUPT_PEARL = new CorruptPearl();

    // Obsidian
    public static final ComplexItem ENCHANTED_OBSIDIAN = new ComplexItem(new ItemSettings()
            .id("enchanted_obsidian")
            .material(Material.OBSIDIAN)
            .glow());

    public static final ComplexItem COMPACT_OBSIDIAN = new ComplexItem(new ItemSettings()
            .id("compact_obsidian")
            .material(Material.PLAYER_HEAD)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE5MjZiZmE5Y2FmOGJjYTkwNjkyNzgwOTc4YjVjNzRkNzEzZTg2NWY1YmRkMzc5MjA5N2IxODc5OTk3ZTU1NyJ9fX0="));

    public static final ComplexItem CORRUPT_OBSIDIAN = new ComplexItem(new ItemSettings()
            .id("corrupt_obsidian")
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .material(Material.PLAYER_HEAD)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFjODc0NTRmMWVlNTg1YjkwZmRiM2EzZTQwOTUyYTVjMmY4MGMwYTQ5ZGZlYzYyODcwZmRmZjE4Mzk2N2E4NCJ9fX0="));

    public static final ComplexItem CRESTFALLEN_MONOLITH = new ComplexItem(new ItemSettings()
            .id("crestfallen_monolith")
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .material(Material.PLAYER_HEAD)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIzOWMwZTUzZTc5ZTdlYmQ0ZGI2YzZkMDk2YzlkOWExNjBjZmYyNzgyMmMwNzdmYjhmNWQ0NTk2OWNjNDk3MiJ9fX0="));

    public static final ComplexItem PAGES_OF_AGONY = new ComplexItem(new ItemSettings()
            .id("pages_of_agony")
            .material(Material.PAPER));

    public static final ComplexItem DIMENSIONAL_JOURNAL = new ComplexItem(new ItemSettings()
            .id("dimensional_journal")
            .material(Material.BOOK)
            .rarity(ComplexItem.Rarity.RARE)
            .stat(Stats.WISDOM, 100d)
            .abilities(new Transcendence())
            .variable(Transcendence.AGONY_PAGES_VARIABLE_TYPE, 5));

    public static final ComplexItem VERTEXICAL_CORE = new ComplexItem(new ItemSettings()
            .id("vertexical_core")
            .material(Material.PLAYER_HEAD)
            .rarity(ComplexItem.Rarity.RARE)
            .skullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWYwMjMwNTExYjg0NGNmM2FmZjBjZWRiNDRjMTMyNDI3OTlkMzMxNTIyMzVmMTdjZWU1NzQ2NTE4NzhlZDVkMCJ9fX0="));

    public static final ComplexItem DEV_STICK = new ComplexItem(new ItemSettings()
            .id("dev_stick")
            .material(Material.STICK)
            .rarity(ComplexItem.Rarity.VERY_SPECIAL)
            .glow()
            .ability(AbilityRegistry.MAGIC_MISSILE_DEV));
    public static final ComplexItem VOLKEN_AXE = new ComplexItem(new ItemSettings()
            .id("volken_axe")
            .material(Material.GOLDEN_AXE)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.AXE));

    public static final ComplexItem VOLCANAXE = new ComplexItem(new ItemSettings()
            .id("volkanaxe")
            .material(Material.GOLDEN_AXE)
            .enchant(Enchantment.DIG_SPEED, 6)
            .rarity(ComplexItem.Rarity.EPIC)
            .type(ComplexItem.Type.AXE));


    public static final ComplexItem CHROMOTONIN = new ComplexItem(new ItemSettings()
            .id("chromotonin")
            .material(Material.LEAD)
            .rarity(ComplexItem.Rarity.OMEGA)
            .type(ComplexItem.Type.MISC));

    public static final ComplexItem VOLKEN_STICK = new ComplexItem(new ItemSettings()
            .id("volken_stick")
            .material(Material.STICK)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.MISC));

    public static final ComplexItem SUPER_SPACE_HELMET = new ComplexItem(new ItemSettings()
            .id("super_space_helmet")
            .material(Material.RED_STAINED_GLASS_PANE)
            .stat(Stats.STRENGTH, 1000d)
            .stat(Stats.HEALTH, 1000d)
            .stat(Stats.TOUGHNESS, 1000d)
            .stat(Stats.REGENERATION, 1000d)
            .stat(Stats.WISDOM, 1000d)
            .stat(Stats.LUCK, 1000d)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 100)
            .enchant(Enchantment.OXYGEN, 100)
            .enchant(Enchantment.DEPTH_STRIDER, 100)
            .enchant(Enchantment.WATER_WORKER, 100)
            .modifier(AttributeRegistry.ARMOR, 100d)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 100d)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 100d)
            .rarity(ComplexItem.Rarity.MYTHIC)
            .type(ComplexItem.Type.HELMET));

    public static final ComplexItem LAPIS_ORBITEX = new ComplexItem(new ItemSettings()
            .id("lapis_orbitex")
            .material(Material.BLUE_DYE)
            .rarity(ComplexItem.Rarity.COMMON)
            .type(ComplexItem.Type.ENCHANTING_FUEL)
            .glow()
            .variable(EnchantingGUI.ENCHANT_FUEL_VAR, 5));

    public static final ComplexItem INFUSED_ORBITEX = new ComplexItem(new ItemSettings()
            .id("infused_orbitex")
            .material(Material.PURPLE_DYE)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.ENCHANTING_FUEL)
            .glow()
            .variable(EnchantingGUI.ENCHANT_FUEL_VAR, 15));

    public static final ComplexItem CREATIVE_MIND = new ComplexItem((new ItemSettings()
            .id("creative_mind")
            .material(Material.PAINTING)
            .stat(Stats.WISDOM, 1d)
            .enchant(Enchantment.OXYGEN, 1)
            .rarity(ComplexItem.Rarity.VERY_SPECIAL)
            .type(ComplexItem.Type.MISC)
            .type(ComplexItem.Type.HELMET)));

    public static final ComplexItem SHADOW_BLADE = new ComplexItem(new ItemSettings()
            .id("shadow_blade")
            .material(Material.IRON_SWORD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.SWORD)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 10d)
            .modifier(AttributeRegistry.ATTACK_SPEED, -2.2d)
            .ability(AbilityRegistry.DARK_FURY));

    public static final ComplexItem DARK_ORB = new ComplexItem(new ItemSettings()
            .id("dark_orb")
            .material(Material.ENDER_PEARL));

    public static final ComplexItem HEAT_BLAZED_CORE = new ComplexItem(new ItemSettings()
            .id("heat_blazed_core")
            .material(Material.FIRE_CHARGE));

    public static final ComplexItem GREATSWORD_VOLKUMOS = new ComplexItem(new ItemSettings()
            .id("greatsword_volkumos")
            .material(Material.STONE_SWORD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.SWORD)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 20)
            .modifier(AttributeRegistry.ATTACK_SPEED, -3.2)
            .stat(Stats.STRENGTH, 50d)
            .ability(AbilityRegistry.TERRA_STRIKE));

    public static final ComplexItem BURNING_HELMET = new ComplexItem(new ItemSettings()
            .id("burning_helmet")
            .material(Material.LEATHER_HELMET)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.HELMET)
            .modifier(AttributeRegistry.HEALTH, 5)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 4)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.ROARING_FLAME));

    public static final ComplexItem BURNING_CHESTPLATE = new ComplexItem(new ItemSettings()
            .id("burning_chestplate")
            .material(Material.LEATHER_CHESTPLATE)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.CHESTPLATE)
            .modifier(AttributeRegistry.HEALTH, 12)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 10)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.ROARING_FLAME));

    public static final ComplexItem BURNING_LEGGINGS = new ComplexItem(new ItemSettings()
            .id("burning_leggings")
            .material(Material.LEATHER_LEGGINGS)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.LEGGINGS)
            .modifier(AttributeRegistry.HEALTH, 10)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 8)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.ROARING_FLAME));

    public static final ComplexItem BURNING_BOOTS = new ComplexItem(new ItemSettings()
            .id("burning_boots")
            .material(Material.LEATHER_BOOTS)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.BOOTS)
            .modifier(AttributeRegistry.HEALTH, 5)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 3)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.ROARING_FLAME, AbilityRegistry.LAVA_GLIDER));

    public static final ComplexItem DARKCALLER = new ComplexItem(new ItemSettings()
            .id("darkcaller")
            .material(Material.BEACON)
            .stat(Stats.WISDOM, 50d)
            .enchant(Enchantment.OXYGEN, 1)
            .ability(AbilityRegistry.DARKCALL)
            .rarity(ComplexItem.Rarity.EPIC)
            .type(ComplexItem.Type.STAFF));

    public static final ComplexItem BURNING_SMOKE = new ComplexItem(new ItemSettings()
            .id("burning_smoke")
            .material(Material.GLASS_BOTTLE));

    public static final ComplexItem GILDED_CARROT = new ComplexItem(new ItemSettings()
            .id("gilded_carrot")
            .material(Material.GOLDEN_CARROT)
            .ability(AbilityRegistry.GILDED_CONSUME)
            .rarity(ComplexItem.Rarity.RARE));

    public static final ComplexItem START_BUTTON = new ComplexItem(new ItemSettings()
            .id("start_button")
            .material(Material.REDSTONE)
            .ability(AbilityRegistry.START_BUTTON)
            .rarity(ComplexItem.Rarity.MYTHIC));

    public static final ComplexItem SOUL_STONE = new ComplexItem(new ItemSettings()
            .id("soul_stone")
            .material(Material.NETHER_STAR)
            .ability(AbilityRegistry.PORTALIZER)
            .rarity(ComplexItem.Rarity.MYTHIC));

    public static final ComplexItem ARCANE_JEWEL = new ComplexItem(new ItemSettings()
            .id("arcane_jewel")
            .material(Material.AMETHYST_SHARD)
            .rarity(ComplexItem.Rarity.RARE));

    public static final ComplexItem WHACK_A_STICK = new ComplexItem(new ItemSettings()
            .id("whack_a_stick")
            .material(Material.STICK)
            .rarity(ComplexItem.Rarity.RARE));

    public static final ComplexItem SUPER_SNOW_BLASTER = new ComplexItem(new ItemSettings()
            .id("super_snow_blaster")
            .material(Material.STONE_HOE)
            .rarity(ComplexItem.Rarity.MYTHIC)
            .ability(AbilityRegistry.SNOW_SHOT));

    public static final ComplexItem DIAMANTINE_HELMET = new ComplexItem(new ItemSettings()
            .id("diamantine_helmet")
            .material(Material.DIAMOND_HELMET)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.HELMET)
            .modifier(AttributeRegistry.HEALTH, 5)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.03, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 4)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.DIAMANTINE_SHIELD));

    public static final ComplexItem DIAMANTINE_CHESTPLATE = new ComplexItem(new ItemSettings()
            .id("diamantine_chestplate")
            .material(Material.DIAMOND_CHESTPLATE)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.CHESTPLATE)
            .modifier(AttributeRegistry.HEALTH, 12)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.03, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 10)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.DIAMANTINE_SHIELD));

    public static final ComplexItem DIAMANTINE_LEGGINGS = new ComplexItem(new ItemSettings()
            .id("diamantine_leggings")
            .material(Material.DIAMOND_LEGGINGS)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.LEGGINGS)
            .modifier(AttributeRegistry.HEALTH, 10)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.03, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 8)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.DIAMANTINE_SHIELD));

    public static final ComplexItem DIAMANTINE_BOOTS = new ComplexItem(new ItemSettings()
            .id("diamantine_boots")
            .material(Material.DIAMOND_BOOTS)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.BOOTS)
            .modifier(AttributeRegistry.HEALTH, 5)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.03, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 3)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 1)
            .abilities(AbilityRegistry.DIAMANTINE_SHIELD));

    public static final ComplexItem GOLEM_HEART = new ComplexItem(new ItemSettings()
            .id("golem_heart")
            .material(Material.NETHERITE_INGOT)
            .rarity(ComplexItem.Rarity.RARE));

    public static final ComplexItem GOLEM_HEART_FRAGMENT = new ComplexItem(new ItemSettings()
            .id("golem_heart_fragment")
            .material(Material.NETHERITE_INGOT)
            .rarity(ComplexItem.Rarity.RARE));

    public static final ComplexItem GOLEM_HELMET = new ComplexItem(new ItemSettings()
            .id("golem_helmet")
            .material(Material.IRON_HELMET)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.HELMET)
            .modifier(AttributeRegistry.HEALTH, 10)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.02, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 6)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 2)
            .modifier(AttributeRegistry.STRENGTH, 10)
            .modifier(AttributeRegistry.WISDOM, 10)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.01)
            .abilities(AbilityRegistry.GOLEMS_HEART));

    public static final ComplexItem GOLEM_CHESTPLATE = new ComplexItem(new ItemSettings()
            .id("golem_chestplate")
            .material(Material.IRON_CHESTPLATE)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.CHESTPLATE)
            .modifier(AttributeRegistry.HEALTH, 20)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.04, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 12)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 2)
            .modifier(AttributeRegistry.STRENGTH, 20)
            .modifier(AttributeRegistry.WISDOM, 20)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.02)
            .abilities(AbilityRegistry.GOLEMS_HEART));

    public static final ComplexItem GOLEM_LEGGINGS = new ComplexItem(new ItemSettings()
            .id("golem_leggings")
            .material(Material.IRON_LEGGINGS)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.LEGGINGS)
            .modifier(AttributeRegistry.HEALTH, 15)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.03, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 10)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 2)
            .modifier(AttributeRegistry.STRENGTH, 15)
            .modifier(AttributeRegistry.WISDOM, 15)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.01)
            .abilities(AbilityRegistry.GOLEMS_HEART));

    public static final ComplexItem GOLEM_BOOTS = new ComplexItem(new ItemSettings()
            .id("golem_boots")
            .material(Material.IRON_BOOTS)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.BOOTS)
            .modifier(AttributeRegistry.HEALTH, 10)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.02, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 5)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 2)
            .modifier(AttributeRegistry.STRENGTH, 10)
            .modifier(AttributeRegistry.WISDOM, 10)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.01)
            .abilities(AbilityRegistry.GOLEMS_HEART));


    public static final ComplexItem TITAN_HELMET = new ComplexItem(new ItemSettings()
            .id("titan_helmet")
            .material(Material.NETHERITE_HELMET)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.HELMET)
            .modifier(AttributeRegistry.HEALTH, 15)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.02, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 8)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 3)
            .modifier(AttributeRegistry.STRENGTH, 15)
            .modifier(AttributeRegistry.WISDOM, 15)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.01)
            .abilities(AbilityRegistry.TITANS_HEART));

    public static final ComplexItem TITAN_CHESTPLATE = new ComplexItem(new ItemSettings()
            .id("titan_chestplate")
            .material(Material.NETHERITE_CHESTPLATE)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.CHESTPLATE)
            .modifier(AttributeRegistry.HEALTH, 30)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.04, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 16)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 3)
            .modifier(AttributeRegistry.STRENGTH, 30)
            .modifier(AttributeRegistry.WISDOM, 30)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.02)
            .abilities(AbilityRegistry.TITANS_HEART));

    public static final ComplexItem TITAN_LEGGINGS = new ComplexItem(new ItemSettings()
            .id("titan_leggings")
            .material(Material.NETHERITE_LEGGINGS)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.LEGGINGS)
            .modifier(AttributeRegistry.HEALTH, 22)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.03, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 13)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 3)
            .modifier(AttributeRegistry.STRENGTH, 22)
            .modifier(AttributeRegistry.WISDOM, 22)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.01)
            .abilities(AbilityRegistry.TITANS_HEART));

    public static final ComplexItem TITAN_BOOTS = new ComplexItem(new ItemSettings()
            .id("titan_boots")
            .material(Material.NETHERITE_BOOTS)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.BOOTS)
            .modifier(AttributeRegistry.HEALTH, 15)
            .modifier(AttributeRegistry.MOVEMENT_SPEED, -0.02, AttributeModifier.Operation.ADD_SCALAR)
            .modifier(AttributeRegistry.ARMOR, 7)
            .modifier(AttributeRegistry.ARMOR_TOUGHNESS, 3)
            .modifier(AttributeRegistry.STRENGTH, 15)
            .modifier(AttributeRegistry.WISDOM, 15)
            .modifier(AttributeRegistry.KNOCKBACK_RESISTANCE, 0.01)
            .abilities(AbilityRegistry.TITANS_HEART));

    public static final ComplexItem AMETHYST_CORE = new ComplexItem(new ItemSettings()
            .id("amethyst_core")
            .material(Material.AMETHYST_SHARD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.MISC));

    public static final ComplexItem AMETHYST_STAFF = new ComplexItem(new ItemSettings()
            .id("amethyst_staff")
            .material(Material.AMETHYST_SHARD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.STAFF)
            .modifier(AttributeRegistry.WISDOM, 50)
            .ability(AbilityRegistry.CRYSTAL_SPIKE));

    public static final ComplexItem HARD_STONE = new ComplexItem(new ItemSettings()
            .id("hard_stone")
            .material(Material.STONE)
            .rarity(ComplexItem.Rarity.COMMON)
            .type(ComplexItem.Type.MISC));

    public static final ComplexItem CRYSTAL_CAPACITOR = new ComplexItem(new ItemSettings()
            .id("crystal_capacitor")
            .material(Material.DIAMOND)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.MISC));

    public static final ComplexItem AMETHYST_ELIXIR = new ComplexItem(new ItemSettings()
            .id("amethyst_elixir")
            .material(Material.POTION)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.MISC)
            .ability(AbilityRegistry.MANA_BOOST));

    public static final ComplexItem COLOSSAL_BLADE = new ComplexItem(new ItemSettings()
            .id("colossal_blade")
            .material(Material.DIAMOND_SWORD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.SWORD)
            .modifier(AttributeRegistry.STRENGTH, 50)
            .modifier(AttributeRegistry.WISDOM, 50)
            .ability(AbilityRegistry.COLOSSAL_SWEEP));

    // Compacted versions of amethyst crystals
    public static final ComplexItem ENCHANTED_AMETHYST_CRYSTAL = new ComplexItem(new ItemSettings()
            .id("enchanted_amethyst_crystal")
            .material(Material.SMALL_AMETHYST_BUD)
            .rarity(ComplexItem.Rarity.COMMON)
            .type(ComplexItem.Type.MISC));

    public static final ComplexItem FLAWLESS_AMETHYST_CRYSTAL = new ComplexItem(new ItemSettings()
            .id("flawless_amethyst_crystal")
            .material(Material.MEDIUM_AMETHYST_BUD)
            .rarity(ComplexItem.Rarity.COMMON)
            .type(ComplexItem.Type.MISC));

    // second tier
    public static final ComplexItem PRISTINE_AMETHYST_CRYSTAL = new ComplexItem(new ItemSettings()
            .id("pristine_amethyst_crystal")
            .material(Material.LARGE_AMETHYST_BUD)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.MISC));

    // Vanilla Items
    public static final VanillaItem LAPIS_LAZULI = new VanillaItem(new ItemSettings()
            .material(Material.LAPIS_LAZULI)
            .variable(EnchantingGUI.ENCHANT_FUEL_VAR, 1));

    public static final ComplexItem VERTEXICAL_BLADE = new ComplexItem(new ItemSettings()
            .id("vertex_blade")
            .material(Material.AMETHYST_SHARD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.MISC)
            .modifier(AttributeRegistry.STRENGTH, 50d)
            .modifier(AttributeRegistry.WISDOM, 100d)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 12)
            .modifier(AttributeRegistry.ATTACK_SPEED, -2)
            .ability(AbilityRegistry.VERTEX_ABILITY));

    public static final ComplexItem THUNDERSTRIKE_STAFF = new ComplexItem(new ItemSettings()
            .id("thunderstrike_staff")
            .material(Material.AMETHYST_SHARD)
            .rarity(ComplexItem.Rarity.RARE)
            .type(ComplexItem.Type.STAFF)
            .modifier(AttributeRegistry.WISDOM, 50)
            .modifier(AttributeRegistry.ATTACK_DAMAGE, 12)
            .ability(AbilityRegistry.THUNDERSTRIKE));
    public static final ComplexItem FROSTFIRE_BOW = new ComplexItem(new ItemSettings()
            .id("frostfire_bow")
            .material(Material.BOW)
            .rarity(ComplexItem.Rarity.UNCOMMON)
            .type(ComplexItem.Type.BOW)
            .ability(new ElementalFlux())
            .modifier(AttributeRegistry.STRENGTH, 10)
            .variable(ElementalFlux.FLUX_VARIABLE_TYPE, ElementalFlux.Flux.FIRE)
            .ability(AbilityRegistry.RAPID_FIRE)
    );

    private final static List<Recipe> registeredRecipes = new ArrayList<>();

    @Deprecated
    public static List<Recipe> registerRecipes() {
        for (ComplexItem item : ComplexItem.itemRegistry) {
            registeredRecipes.addAll(item.getRecipes());
        }

        for (Recipe recipe : registeredRecipes) {
            try {
                Bukkit.addRecipe(recipe);
            } catch (IllegalStateException e) {
                if (recipe instanceof Keyed) {
                    //Util.logToConsole("Found duplicate recipe, re-adding.");
                    Bukkit.removeRecipe(((Keyed) recipe).getKey());
                    Bukkit.addRecipe(recipe);
                }

            }
        }
        return registeredRecipes;
    }

    public static void registerItems() {
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + ComplexItem.itemRegistry.size() + ChatColor.WHITE + " items.");
    }

    @Nullable
    public static ComplexItem byItem(ItemStack item) {
        try {
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
            String id = container.get(ComplexItem.GLOBAL_ID, PersistentDataType.STRING);
            return byId(id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static ComplexItem byId(String id) {
        for (ComplexItem item : ComplexItem.itemRegistry) {
            if (id == null) return null;
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }
}
