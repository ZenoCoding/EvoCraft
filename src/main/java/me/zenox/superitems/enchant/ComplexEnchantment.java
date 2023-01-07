package me.zenox.superitems.enchant;

import me.zenox.superitems.Slot;
import me.zenox.superitems.attribute.AttributeModifier;
import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemMeta;
import me.zenox.superitems.persistence.SerializedPersistentType;
import me.zenox.superitems.util.QuadConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class ComplexEnchantment {
    private final String id;
    private final TranslatableText name;
    private final int maxLevel;
    // WEIGHT, not rarity (smaller is rarer)
    private final int weight;
    private final List<ComplexItem.Type> types;
    private final List<Slot> slots;
    private final List<AttributeModifier> stats;
    private final QuadConsumer<Event, Integer, ItemStack, Player> executable;
    private final Enchantment vanillaEnchant;
    private final List<EnchantRegistry.EnchantmentWrapper> exclusive;

    private final Class<? extends Event> eventType;
    public static final List<Class<? extends Event>> registeredEvents = new ArrayList<>();

    private static final List<ComplexEnchantment> registeredEnchants = new ArrayList<>();
    // List of all enchants that have an executable
    private static final List<ComplexEnchantment> executableEnchants = new ArrayList<>();

    /**
     * New instance of ComplexEnchantment
     *
     * @param id
     * @param maxLevel
     * @param weight
     * @param types
     * @param slot
     * @param stats
     * @param executable
     * @param vanillaEnchant
     * @param exclusive
     * @param eventType
     */
    public ComplexEnchantment(String id, int maxLevel, int weight, List<ComplexItem.Type> types, List<Slot> slot, List<AttributeModifier> stats, QuadConsumer<Event, Integer, ItemStack, Player> executable, Enchantment vanillaEnchant, List<EnchantRegistry.EnchantmentWrapper> exclusive, Class<? extends Event> eventType){
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.ENCHANT_NAME + "-" + id);
        this.maxLevel = maxLevel;
        this.weight = weight;
        this.types = types;
        this.slots = slot;
        this.stats = stats;
        this.executable = executable;
        this.vanillaEnchant = vanillaEnchant;
        this.exclusive = exclusive;
        this.eventType = eventType;

        for (ComplexEnchantment enchantment :
                registeredEnchants) {
            if (enchantment.getId().equalsIgnoreCase(id)) {
                Util.logToConsole("Duplicate Enchantment ID: " + id + " | Exact Match: " + enchantment.equals(this));
                throw new IllegalArgumentException("Enchantment ID cannot be duplicate");
            }
        }

        registeredEnchants.add(this);
        if(this.executable != null) {
            executableEnchants.add(this);
            registeredEvents.add(eventType);
        }
    }

    public ComplexEnchantment(@NotNull EnchantmentSettings settings, Class<? extends Event> eventType) {
        this(settings.getId(), settings.getMaxLevel(), settings.getRarity(), settings.getTypes(), settings.getSlots(), settings.getStats(), settings.getExecutable(), settings.getVanillaEnchant(), settings.getExclusive(), eventType);
    }

    public ComplexEnchantment(String id, int maxLevel, int weight, List<ComplexItem.Type> types, Slot slot, List<AttributeModifier> stats, QuadConsumer<Event, Integer, ItemStack, Player> executable, Enchantment vanillaEnchant, List<EnchantRegistry.EnchantmentWrapper> exclusive, Class<? extends Event> eventType){
        this(id, maxLevel, weight, types, List.of(slot), stats, executable, vanillaEnchant, exclusive, eventType);
    }

    @Nullable
    public static ComplexEnchantment byId(String id){
        return registeredEnchants.stream()
                .filter(enchantment -> enchantment.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    @Nullable
    public static ComplexEnchantment byVanillaEnchantment(Enchantment enchantment){
        return registeredEnchants.stream()
                .filter(enchantment1 -> enchantment.equals(enchantment1.getVanillaEnchant()))
                .findFirst().orElse(null);
    }

    public static List<ComplexEnchantment> getRegisteredEnchants() {
        return registeredEnchants;
    }

    public static List<ComplexEnchantment> getExecutableEnchants() {
        return executableEnchants;
    }

    public void useEnchant(Event e){
        if (!this.eventType.isInstance(e)) return;
        if (!checkEvent(e)) return;
        Player p = getPlayerOfEvent(e);
        List<ItemStack> items = new ArrayList<>();
        for (Slot slot : this.slots){
            items.addAll(slot.item(p));
        }
        for (ItemStack item : items){
            if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) continue;
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            HashMap<ComplexEnchantment, Integer> enchantmentMap = container.get(ComplexItemMeta.ENCHANT_KEY, new SerializedPersistentType<>());
            if(enchantmentMap.keySet().contains(this.id)){
                int level = enchantmentMap.get(this.id);
                this.executable.accept(e, level, item, p);
            }
        }
    }

    abstract boolean checkEvent(Event e);

    abstract Player getPlayerOfEvent(Event e);


    public String getId() {
        return id;
    }

    public TranslatableText getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getWeight() {
        return weight;
    }

    public List<ComplexItem.Type> getTypes() {
        return types;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public List<AttributeModifier> getStats() {
        return stats;
    }

    public QuadConsumer<Event, Integer, ItemStack, Player> getExecutable() {
        return executable;
    }

    public Enchantment getVanillaEnchant() {
        return vanillaEnchant;
    }

    public List<ComplexEnchantment> getExclusive() {
        return exclusive.stream()
                .map(EnchantRegistry.EnchantmentWrapper::getEnchant)
                .toList();
    }
}
