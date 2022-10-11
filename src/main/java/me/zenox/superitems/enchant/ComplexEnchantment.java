package me.zenox.superitems.enchant;

import com.archyx.aureliumskills.modifier.StatModifier;
import me.zenox.superitems.Slot;
import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.util.Util;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


public class ComplexEnchantment {
    private final String id;
    private final TranslatableText name;
    private final int maxLevel;
    private final List<ComplexItem.Type> types;
    private final Slot slot;
    private final List<StatModifier> stats;
    private final BiConsumer<Event, Integer> executable;

    private final Class<? extends Event> eventType;

    private static final List<ComplexEnchantment> registeredEnchants = new ArrayList<>();

    public ComplexEnchantment(String id, int maxLevel, List<ComplexItem.Type> types, Slot slot, List<StatModifier> stats, BiConsumer<Event, Integer> executable, Class<? extends Event> eventType){
        this.id = id;
        this.name = new TranslatableText(TranslatableText.TranslatableType.ENCHANT_NAME + "-" + id);
        this.maxLevel = maxLevel;
        this.types = types;
        this.slot = slot;
        this.stats = stats;
        this.executable = executable;
        this.eventType = eventType;

        for (ComplexEnchantment enchantment :
                registeredEnchants) {
            if (enchantment.getId().equalsIgnoreCase(id)) {
                Util.logToConsole("Duplicate Enchantment ID: " + id + " | Exact Match: " + enchantment.equals(this));
                throw new IllegalArgumentException("Enchantment ID cannot be duplicate");
            }
        }

        registeredEnchants.add(this);
    }

    @Nullable
    public static ComplexEnchantment byId(String id){
        try {
            return registeredEnchants.stream()
                    .filter(enchantment -> enchantment.getId().equalsIgnoreCase(id))
                    .toList().get(0);
        } catch(ArrayIndexOutOfBoundsException ignored){
            return null;
        }
    }

    public static List<ComplexEnchantment> getRegisteredEnchants() {
        return registeredEnchants;
    }

    public String getId() {
        return id;
    }

    public TranslatableText getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<ComplexItem.Type> getTypes() {
        return types;
    }

    public Slot getSlot() {
        return slot;
    }

    public List<StatModifier> getStats() {
        return stats;
    }

    public BiConsumer<Event, Integer> getExecutable() {
        return executable;
    }
}
