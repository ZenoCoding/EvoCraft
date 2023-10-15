package me.zenox.evocraft.item;

import me.zenox.evocraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

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
        List<Material> swordList = List.of(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD);
        List<Material> pickaxeList = List.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);
        List<Material> axeList = List.of(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
        List<Material> shovelList = List.of(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL);
        List<Material> hoeList = List.of(Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE);
        List<Material> helmetList = List.of(Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET);
        List<Material> chestplateList = List.of(Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE);
        List<Material> leggingsList = List.of(Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS);
        List<Material> bootsList = List.of(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS);
        List<Material> bowList = List.of(Material.BOW);
        List<Material> crossbowList = List.of(Material.CROSSBOW);
        List<Material> tridentList = List.of(Material.TRIDENT);
        List<Material> shieldList = List.of(Material.SHIELD);
        List<Material> fishingRodList = List.of(Material.FISHING_ROD);
        swordList.forEach(material -> typeMap.put(material, Type.SWORD));
        pickaxeList.forEach(material -> typeMap.put(material, Type.PICKAXE));
        axeList.forEach(material -> typeMap.put(material, Type.AXE));
        shovelList.forEach(material -> typeMap.put(material, Type.SHOVEL));
        hoeList.forEach(material -> typeMap.put(material, Type.HOE));
        helmetList.forEach(material -> typeMap.put(material, Type.HELMET));
        chestplateList.forEach(material -> typeMap.put(material, Type.CHESTPLATE));
        leggingsList.forEach(material -> typeMap.put(material, Type.LEGGINGS));
        bootsList.forEach(material -> typeMap.put(material, Type.BOOTS));
        bowList.forEach(material -> typeMap.put(material, Type.BOW));
        crossbowList.forEach(material -> typeMap.put(material, Type.CROSSBOW));
        tridentList.forEach(material -> typeMap.put(material, Type.TRIDENT));
        shieldList.forEach(material -> typeMap.put(material, Type.SHIELD));
        fishingRodList.forEach(material -> typeMap.put(material, Type.FISHING_ROD));

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

    /**
     * Vanilla item constructor to override the default settings of a vanilla item.
     * @param settings The settings to override.
     */
    public VanillaItem(ItemSettings settings) {
        super(settings.id(settings.getMaterial().getKey().getKey()), true);
        if (!settings.getMaterial().isItem()) throw new IllegalArgumentException("Material must have an item form");
        for (VanillaItem item : new ArrayList<>(vanillaItemList)) {
            // remove the old item
            if (Objects.equals(item.getId(), this.getId()))
                vanillaItemList.remove(item);
        }
        vanillaItemList.add(this);
    }

    @Override
    public String getDisplayName() {
        return Objects.requireNonNullElse(Bukkit.getItemFactory().getItemMeta(getMaterial()).getDisplayName(), getMaterial().name().replace("_", " ").toLowerCase());
    }

    public static VanillaItem of(Material material) {
            return vanillaItemList.stream()
                    .filter(vanillaItem -> Objects.equals(vanillaItem.getId(), material.getKey().getKey()))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("VanillaItem with material " + material + " does not exist"));
    }

    @Nullable
    public static VanillaItem byItem(ItemStack item) {
        try {
            return vanillaItemList.stream()
                    .filter(vanillaItem ->
                            Objects.equals(vanillaItem.getId(), Objects.requireNonNull(item.getItemMeta())
                                    .getPersistentDataContainer()
                                    .get(ComplexItem.GLOBAL_ID, PersistentDataType.STRING))).findFirst().orElse(null);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void registerItems(){
        Arrays.stream(Material.values()).filter(material -> material.isItem()).forEach(material -> new VanillaItem(material));
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + Material.values().length + ChatColor.WHITE + " vanilla based ComplexItems.");
    }

}
