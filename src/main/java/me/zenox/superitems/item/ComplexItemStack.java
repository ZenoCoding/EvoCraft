package me.zenox.superitems.item;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.modifier.StatModifier;
import com.archyx.aureliumskills.stats.Stat;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.abilities.Ability;
import me.zenox.superitems.persistence.SerializedPersistentType;
import me.zenox.superitems.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class that represents a "custom" ItemStack.
 *
 * Converted and loaded upon loading a "basic" ItemStack
 * Contains data about the item that Minecraft/Bukkit doesn't store, ie. Upgrades, Abilities, etc.
 * Also used for converting/updating "custom" items and differing them from normal minecraft items, containing Utility methods to manipulate the item and then update it in Minecraft
 *
 * Planned for future implementation
 */
public class ComplexItemStack implements Cloneable{

    // Bound ItemStack that represents this ComplexItemStack
    private ItemStack item;
    private final ComplexItem complexItem;
    private final UUID uuid;
    private ComplexItemMeta complexMeta;
    private final List<Ability> abilities;

    private String skullURL = "";

    public ComplexItemStack(ComplexItem complexItem, int amount){
        this(complexItem, new ItemStack(complexItem.getMaterial()));
        this.item = buildItem(amount);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(complexItem.getDisplayName());

        item.setItemMeta(meta);
    }

    public ComplexItemStack(ComplexItem complexItem, ItemStack item){
        this.complexItem = complexItem;
        this.uuid = complexItem.isUnique() ? UUID.randomUUID() : null;
        this.abilities = complexItem.getAbilities() == null ? new ArrayList<>() : new ArrayList<>(complexItem.getAbilities());
        this.skullURL = complexItem.getSkullURL();
        this.item = item;

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(complexItem.getDisplayName());

        item.setItemMeta(meta);

        this.complexMeta = new ComplexItemMeta(this);

        Util.logToConsole("Item Name: " + meta.getDisplayName());
    }

    private ItemStack buildItem(int amount){
        item = new ItemStack(this.complexItem.getMaterial());

        // Set Initial ComplexItem Modifications
        item.setItemMeta(complexItem.getMeta());

        ItemMeta meta = item.getItemMeta();

        // Set CustomModelData
        meta.setCustomModelData(complexItem.getCustomModelData());

        item.setAmount(amount);

        // Set ItemMeta so that SkillModifier can use it
        item.setItemMeta(meta);

        for (Map.Entry<Stat, Double> entry : complexItem.getStats().entrySet()) {
            item = complexItem.getType().isWearable() ? AureliumAPI.addArmorModifier(item, entry.getKey(), entry.getValue(), false) : AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), false);
        }

        meta = item.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(ComplexItem.GLOBAL_ID, PersistentDataType.STRING, this.getId());

        if(this.uuid != null) container.set(new NamespacedKey(SuperItems.getPlugin(), "uuid"), new SerializedPersistentType<UUID>(), uuid);

        // Set ItemMeta so that ComplexItemMeta can use it
        item.setItemMeta(meta);

        complexMeta = new ComplexItemMeta(this);

        complexMeta.setVariable(ComplexItemMeta.RARITY_VAR, complexItem.getRarity());
        complexMeta.setVariable(ComplexItemMeta.TYPE_VAR, complexItem.getType());

        complexMeta.updateItem();

        item = this.skullURL.isBlank() ? item : Util.makeSkull(item, this.skullURL);

        return item;
    }

    public ComplexItemStack update(boolean force){
        ItemMeta meta = item.getItemMeta();

        // Set CustomModelData
        meta.setCustomModelData(complexItem.getCustomModelData());

        // Take attributes/enchants from it's previous state
//        for (Map.Entry<Enchantment, Integer> enchant :
//                meta.getEnchants().entrySet()) {
//            meta.removeEnchant(enchant.getKey());
//        }
//
//        for (Map.Entry<Enchantment, Integer> enchant :
//                this.item.getItemMeta().getEnchants().entrySet()) {
//            meta.addEnchant(enchant.getKey(), enchant.getValue(), true);
//        }

        for (Map.Entry<Stat, Double> entry : complexItem.getStats().entrySet()) {
            item = complexItem.getType().isWearable() ? AureliumAPI.addArmorModifier(item, entry.getKey(), entry.getValue(), false) : AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), false);
            meta = item.getItemMeta();
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(ComplexItem.GLOBAL_ID, PersistentDataType.STRING, this.getId());

        if(this.uuid != null) container.set(new NamespacedKey(SuperItems.getPlugin(), "uuid"), new SerializedPersistentType<UUID>(), uuid);

        // Set ItemMeta so that ComplexItemMeta can use it
        item.setItemMeta(meta);

        complexMeta = new ComplexItemMeta(this);

        complexMeta.setVariable(ComplexItemMeta.RARITY_VAR, complexItem.getRarity());
        complexMeta.setVariable(ComplexItemMeta.TYPE_VAR, complexItem.getType());

        complexMeta.updateItem();

        item = this.skullURL.isBlank() ? item : Util.makeSkull(item, this.skullURL);

        return this;
    }

    @Nullable
    public static ComplexItemStack of(ItemStack item){
        ComplexItem complexItem = ItemRegistry.getBasicItemFromItemStack(item);
        if(complexItem == null) {
            Util.logToConsole("Returned null because item " + item.getItemMeta().getDisplayName() + " had no complex registry.");
            return null;
        }
        ComplexItemStack complexItemStack = new ComplexItemStack(complexItem, item);
        return complexItemStack;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getId() {
        return complexItem.getId();
    }

    public NamespacedKey getKey() {
        return this.complexItem.getKey();
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }

    public ComplexItem getComplexItem() {
        return complexItem;
    }

    public ComplexItemMeta getComplexMeta() {
        return complexMeta;
    }
}