package me.zenox.superitems.item;

import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

/**
 * Complex representation of a vanilla item.
 */
public class VanillaItem extends ComplexItem {

    private static final List<VanillaItem> vanillaItemList = new ArrayList<>();

    /**
     * Maps every material to a {@link ComplexItem.Type}.
     */
    private static final Map<Material, Type> typeMap = new HashMap<>();
    /**
     * Maps every material to a {@link ComplexItem.Rarity}.
     */
    private static final Map<Material, Rarity> rarityMap = new HashMap<>();

    static {
        Arrays.stream(Material.values()).forEach(material -> rarityMap.put(material, Rarity.COMMON));
        List<Material> uncommonList = List.of(Material.NETHER_STAR, Material.TOTEM_OF_UNDYING, Material.BEACON);
        // Unobtainable items i.e. barrier, bedrock, etc.
        List<Material> unobtainableList = List.of(Material.COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.COMMAND_BLOCK_MINECART, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID, Material.JIGSAW, Material.LIGHT, Material.DEBUG_STICK, Material.KNOWLEDGE_BOOK,
                Material.ELYTRA, Material.SPAWNER, Material.BARRIER, Material.BEDROCK, Material.END_PORTAL_FRAME, Material.REINFORCED_DEEPSLATE, Material.PETRIFIED_OAK_SLAB);
        uncommonList.forEach(material -> rarityMap.put(material, Rarity.UNCOMMON));
        unobtainableList.forEach(material -> rarityMap.put(material, Rarity.UNOBTAINABLE));

        Arrays.stream(Material.values()).forEach(material -> typeMap.put(material, Type.MISC));

        vanillaItemList.removeIf(vanillaItem -> vanillaItem.getMaterial() == Material.AIR);
    }

    public VanillaItem(Material material) {
        super(material.getKey().getKey(), rarityMap.get(material), typeMap.get(material), material);
        if (!material.isItem()) throw new IllegalArgumentException("Material must have an item form");
        for (VanillaItem item : vanillaItemList) {
            // might be redundant behavior that mimics ComplexItem
            if (Objects.equals(item.getId(), this.getId()))
                throw new IllegalArgumentException("A VanillaItem has already been created with this material.");
        }
        vanillaItemList.add(this);
    }

    @Override
    public String getDisplayName() {
        return Objects.requireNonNullElse(Bukkit.getItemFactory().getItemMeta(getMaterial()).getDisplayName(), getMaterial().name().replace("_", " ").toLowerCase());
    }

    public static ComplexItem of(Material material) {
        try {
            return vanillaItemList.stream().filter(vanillaItem -> Objects.equals(vanillaItem.getId(), material.getKey().getKey())).toList().get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("VanillaItem with material " + material + " does not exist");
        }
    }


    public static void registerItems(){
        Arrays.stream(Material.values()).filter(material -> material.isItem()).forEach(material -> new VanillaItem(material));
        Util.logToConsole(ChatColor.WHITE + "Registered " + ChatColor.GOLD + Material.values().length + ChatColor.WHITE + " vanilla based ComplexItems.");
    }

}
