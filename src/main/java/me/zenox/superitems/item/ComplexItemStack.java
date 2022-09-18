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
    private ComplexItem.Rarity rarity;
    private final ComplexItem.Type type;
    private ItemMeta meta;
    private ComplexItemMeta complexMeta;
    private final Map<Stat, Double> stats;
    private final List<Ability> abilities;

    private String skullURL = "";

    public ComplexItemStack(ComplexItem complexItem, int amount){
        this(complexItem, new ItemStack(complexItem.getMaterial()));
        this.item = buildItem(amount);
    }

    public ComplexItemStack(ComplexItem complexItem, ItemStack item){
        this.complexItem = complexItem;
        this.uuid = complexItem.isUnique() ? UUID.randomUUID() : null;
        this.rarity = complexItem.getRarity();
        this.type = complexItem.getType();
        this.meta = complexItem.getMeta();
        this.stats = complexItem.getStats();
        this.abilities = complexItem.getAbilities() == null ? new ArrayList<>() : new ArrayList<>(complexItem.getAbilities());
        this.skullURL = complexItem.getSkullURL();

        this.meta.setDisplayName(complexItem.getDisplayName());

        this.item = item;

        this.complexMeta = new ComplexItemMeta(meta, abilities, item);

        Util.logToConsole("Item Name: " + this.meta.getDisplayName());
    }

    private ItemStack buildItem(int amount){
        ItemStack item = new ItemStack(this.complexItem.getMaterial());

        // Set CustomModelData
        this.meta.setCustomModelData(complexItem.getCustomModelData());


        item.setItemMeta(this.meta);
        item.setAmount(amount);

        for (Map.Entry<Stat, Double> entry : this.getStats().entrySet()) {
            item = this.getType().isWearable() ? AureliumAPI.addArmorModifier(item, entry.getKey(), entry.getValue(), false) : AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), false);
            this.meta = item.getItemMeta();
        }

        complexMeta = new ComplexItemMeta(this.meta, this.abilities, item);

        ItemMeta itemMeta = complexMeta.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        container.set(ComplexItem.GLOBAL_ID, PersistentDataType.STRING, this.getId());

        if(this.uuid != null) container.set(new NamespacedKey(SuperItems.getPlugin(), "uuid"), new SerializedPersistentType<UUID>(), uuid);

        ItemStack skullItem = Util.makeSkull(item, this.skullURL);

        complexMeta.setVariable(ComplexItemMeta.RARITY_VAR, rarity);
        complexMeta.setVariable(ComplexItemMeta.TYPE_VAR, type);

        item = this.skullURL.isBlank() ? item : skullItem;

        complexMeta.updateItem();

        this.item = item;

        return item;
    }

    public ComplexItemStack update(boolean force){
        //ItemStack item = new ItemStack(this.complexItem.getMaterial());

        // Set CustomModelData
        this.meta.setCustomModelData(complexItem.getCustomModelData());

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

        item.setItemMeta(this.meta);


        for (Map.Entry<Stat, Double> entry : this.getStats().entrySet()) {
            item = this.getType().isWearable() ? AureliumAPI.addArmorModifier(item, entry.getKey(), entry.getValue(), false) : AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), false);
            this.meta = item.getItemMeta();
        }

        complexMeta = new ComplexItemMeta(this.meta, this.abilities, item);

        ItemMeta itemMeta = complexMeta.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        container.set(ComplexItem.GLOBAL_ID, PersistentDataType.STRING, this.getId());

        if(this.uuid != null) container.set(new NamespacedKey(SuperItems.getPlugin(), "uuid"), new SerializedPersistentType<UUID>(), uuid);

        ItemStack skullItem = Util.makeSkull(item, this.skullURL);

        complexMeta.setVariable(ComplexItemMeta.RARITY_VAR, rarity);
        complexMeta.setVariable(ComplexItemMeta.TYPE_VAR, type);

        item = this.skullURL.isBlank() ? item : skullItem;

        complexMeta.read(force);

        this.item.setItemMeta(item.getItemMeta());

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

    public String getDisplayName() {
        return this.rarity.color() + this.meta.getDisplayName();
    }

    public String getId() {
        return complexItem.getId();
    }

    public ComplexItem.Rarity getRarity() {
        return rarity;
    }

    public void setRarity(ComplexItem.Rarity rarity) {
        this.rarity = rarity;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public ComplexItem.Type getType() {
        return this.type;
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public NamespacedKey getKey() {
        return this.complexItem.getKey();
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }
}