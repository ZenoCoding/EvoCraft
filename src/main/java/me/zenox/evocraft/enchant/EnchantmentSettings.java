package me.zenox.evocraft.enchant;

import me.zenox.evocraft.Slot;
import me.zenox.evocraft.attribute.AttributeModifier;
import me.zenox.evocraft.item.ComplexItem;
import me.zenox.evocraft.util.QuadConsumer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class to use the builder pattern to create enchantments
 * Constructors are ugly!
 */
public class EnchantmentSettings {

    private String id;
    private int maxLevel;
    // WEIGHT, not rarity (smaller is rarer)
    private int rarity;
    private List<ComplexItem.Type> types;
    private List<Slot> slots;
    private List<AttributeModifier> stats;
    private QuadConsumer<Event, Integer, ItemStack, Player> executable;
    private Enchantment vanillaEnchant;
    private List<EnchantRegistry.EnchantmentWrapper> exclusive;


    public EnchantmentSettings() {
        this.id = "enchantment_" + UUID.randomUUID();
        this.maxLevel = 1;
        this.rarity = 100;
        this.types = new ArrayList<>(List.of(ComplexItem.Type.values()));
        this.types.remove(ComplexItem.Type.MISC);
        this.slots = new ArrayList<>();
        this.slots.add(Slot.MAIN_HAND);
        this.stats = new ArrayList<>();
        this.exclusive = new ArrayList<>();
        this.executable = null;
    }

    public String getId() {
        return id;
    }

    public EnchantmentSettings id(String id) {
        this.id = id;
        return this;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public EnchantmentSettings maxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    public int getRarity() {
        return rarity;
    }

    public EnchantmentSettings rarity(int rarity) {
        this.rarity = rarity;
        return this;
    }

    public List<ComplexItem.Type> getTypes() {
        return types;
    }

    public EnchantmentSettings type(ComplexItem.Type type) {
        this.types = new ArrayList<>(List.of(type));
        return this;
    }

    public EnchantmentSettings types(ComplexItem.Type ... types) {
        this.types = new ArrayList<>(List.of(types));
        return this;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public EnchantmentSettings slot(Slot slot) {
        this.slots.add(slot);
        return this;
    }

    public EnchantmentSettings slots(Slot ... slots) {
        this.slots.addAll(List.of(slots));
        return this;
    }

    public List<AttributeModifier> getStats() {
        return stats;
    }

    public EnchantmentSettings stat(AttributeModifier stat) {
        this.stats.add(stat);
        return this;
    }

    public EnchantmentSettings stats(AttributeModifier ... stats) {
        this.stats.addAll(List.of(stats));
        return this;
    }

    public QuadConsumer<Event, Integer, ItemStack, Player> getExecutable() {
        return executable;
    }

    public EnchantmentSettings executable(QuadConsumer<Event, Integer, ItemStack, Player> executable) {
        this.executable = executable;
        return this;
    }

    public Enchantment getVanillaEnchant() {
        return vanillaEnchant;
    }

    public EnchantmentSettings vanillaEnchant(Enchantment vanillaEnchant) {
        this.vanillaEnchant = vanillaEnchant;
        return this;
    }

    public List<EnchantRegistry.EnchantmentWrapper> getExclusive() {
        return exclusive;
    }

    public EnchantmentSettings exclusive(EnchantRegistry.EnchantmentWrapper ... exclusive) {
        this.exclusive.addAll(List.of(exclusive));
        return this;
    }
}