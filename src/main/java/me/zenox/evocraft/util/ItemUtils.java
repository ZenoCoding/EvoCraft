package me.zenox.evocraft.util;

import org.bukkit.Material;
import java.util.Set;
import java.util.EnumSet;

public class ItemUtils {
    private static final Set<Material> HANDHELD_ITEMS = EnumSet.of(
            // Tools
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
            Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
            Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
            Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE,

            // Weapons
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.BOW, Material.CROSSBOW, Material.TRIDENT,

            // rod like items
            Material.BLAZE_ROD, Material.STICK,

            // Others
            Material.FISHING_ROD, Material.CARROT_ON_A_STICK, Material.WARPED_FUNGUS_ON_A_STICK,
            Material.SHEARS
    );

    public static boolean isHandheld(Material material) {
        return HANDHELD_ITEMS.contains(material);
    }
}