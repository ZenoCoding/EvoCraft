package me.zenox.superitems.items;

import me.zenox.superitems.items.armoritems.*;
import me.zenox.superitems.items.basicitems.*;
import me.zenox.superitems.items.superitems.*;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemRegistry {


    private final static List<ComplexItem> registeredItems = new ArrayList<>();
    public static final ComplexItem GARDENER_SAPLING = registerItem(new GardenerSapling());
    public static final ComplexItem ENCHANTED_MAGMA_BLOCK = registerItem(new ComplexItem(new ItemSettings().name("Enchanted Magma Block").id("enchanted_magma_block").material(Material.MAGMA_BLOCK).glow()));
    public static final ComplexItem PURIFIED_MAGMA_DISTILLATE = registerItem(new PurifiedMagmaDistillate());
    public static final ComplexItem ENCHANTED_BLAZE_ROD = registerItem(new ComplexItem(new ItemSettings().name("Enchanted Blaze Rod").id("enchanted_blaze_rod").material(Material.BLAZE_ROD).glow()));
    public static final ComplexItem BURNING_ASHES = registerItem(new BurningAshes());
    public static final ComplexItem MOLTEN_POWDER = registerItem(new MoltenPowder());
    public static final ComplexItem ENCHANTED_ENDER_PEARL = registerItem(new EnchantedEnderPearl());
    public static final ComplexItem COMPACTED_ENDER_PEARL = registerItem(new CompactedEnderPearl());
    public static final ComplexItem ABSOLUTE_ENDER_PEARL = registerItem(new AbsoluteEnderPearl());
    public static final ComplexItem DARK_SKULL = registerItem(new DarkSkull());
    public static final ComplexItem TORMENTED_SOUL = registerItem(new TormentedSoul());
    public static final ComplexItem ENCHANTED_IRON_BLOCK = registerItem(new EnchantedIronBlock());
    public static final ComplexItem MAGIC_TOY_STICK = registerItem(new ToyStick());
    public static final ComplexItem SOUL_CRYSTAL = registerItem(new SoulCrystal());
    public static final ComplexItem FIERY_EMBER_STAFF = registerItem(new FieryEmberStaff());
    public static final ComplexItem DARK_EMBER_STAFF = registerItem(new DarkEmberStaff());
    public static final ComplexItem TORMENTED_BLADE = registerItem(new TormentedBlade());
    public static final ComplexItem SPEEDY_GONZALES = registerItem(new SpeedyGonzales());
    public static final ComplexItem JACKASS_GONZALES = registerItem(new JackassGonzales());
    public static final ComplexItem SWORD_OF_JUSTICE = registerItem(new SwordOfJustice());
    public static final ComplexItem CRUCIFIED_AMULET = registerItem(new CrucifiedAmulet());
    public static final ComplexItem DESECRATOR_HELMET = registerItem(new DesecratorHelmet());
    public static final ComplexItem DESECRATOR_CHESTPLATE = registerItem(new DesecratorChestplate());
    public static final ComplexItem DESECRATOR_LEGGINGS = registerItem(new DesecratorLeggings());
    public static final ComplexItem DESECRATOR_BOOTS = registerItem(new DesecratorBoots());
    public static final ComplexItem DESECRATOR_SCALE = registerItem(new DesecratorScale());
    public static final ComplexItem DESECRATOR_CLAW = registerItem(new DesecratorClaw());
    public static final ComplexItem DESECRATOR_TOE = registerItem(new DesecratorToe());
    public static final ComplexItem CRULEN_SHARD = registerItem(new CrulenShard());
    public static final ComplexItem PYTHEMION_GEM = registerItem(new PythemionGem());
    public static final ComplexItem PSYCHEDELIC_ORB = registerItem(new PsychedelicOrb());
    public static final ComplexItem HYPER_CRUX = registerItem(new HyperCrux());
    public static final ComplexItem RAVAGER_SKIN = registerItem(new RavagerSkin());
    public static final ComplexItem TOUGH_FABRIC = registerItem(new ToughFabric());
    public static final ComplexItem KEVLAR = registerItem(new Kevlar());
    public static final ComplexItem TOTEM_POLE = registerItem(new TotemPole());
    public static final ComplexItem CORRUPTED_TOTEM_POLE = registerItem(new CorruptedTotemPole());
    public static final ComplexItem WARPED_CUBE = registerItem(new WarpedCube());
    public static final ComplexItem VOID_STONE = registerItem(new VoidStone());
    public static final ComplexItem VOID_SCEPTER = registerItem(new VoidScepter());
    public static final ComplexItem OBSIDIAN_SCYTHE = registerItem(new ObsidianScythe());

    public static final ComplexItem DEV_STICK = registerItem(new DevStick());

    public  static final  ComplexItem VOID_SCYTHE = registerItem(new VoidScythe());

    public static final ComplexItem FLAMING_HELMET = registerItem(new FlamingHelmet());
    public static final ComplexItem FLAMING_CHESTPLATE = registerItem(new FlamingChestplate());
    public static final ComplexItem FLAMING_LEGGINGS = registerItem(new FlamingLeggings());
    public static final ComplexItem FLAMING_BOOTS = registerItem(new FlamingBoots());

    private final static List<Recipe> registeredRecipes = new ArrayList<>();


    private static ComplexItem registerItem(ComplexItem item) {
        registeredItems.add(item);
        return item;
    }

    public static List<Recipe> registerRecipes() {
        for (ComplexItem item : registeredItems) {
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
                } else { /**Util.logToConsole("Found duplicate recipe that wasn't keyed, skipping.");**/}

            }
        }
        return registeredRecipes;
    }

    public static void registerItems() {
        Util.logToConsole("Registering Custom Items... " + registeredItems.size() + " Items registered.");
    }

    @Nullable
    public static ComplexItem getBasicItemFromItemStack(ItemStack item) {
        try {
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
            String id = container.get(ComplexItem.GLOBAL_ID, PersistentDataType.STRING);
            return getBasicItemFromId(id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static ComplexItem getBasicItemFromId(String id) {
        for (ComplexItem item : registeredItems) {
            if (id == null) return null;
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public static List<ComplexItem> getRegisteredItems() {
        return registeredItems;
    }

}
