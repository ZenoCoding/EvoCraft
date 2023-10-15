package me.zenox.evocraft.attribute;

import me.zenox.evocraft.data.TranslatableText;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Attribute implements Serializable {
    public final static String VALUE_PLACEHOLDER = "{value}";
    public final static List<Attribute> attributeRegistry = new ArrayList<>();

    private final String id;
    private final TranslatableText name;
    private final ChatColor color;
    private final AttributeSource source;

    private final Function<Double, String> valueFormatter;

    public Attribute(String id, ChatColor color, AttributeSource source, Function<Double, String> valueFormatter) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.ATTRIBUTE + "-" + id);
        this.color = color;
        this.source = source;
        this.valueFormatter = valueFormatter;

        for (Attribute attribute :
                attributeRegistry) {
            if (attribute.getId().equalsIgnoreCase(id)) {
                Util.logToConsole("Duplicate Attribute ID: " + id + " | Exact Match: " + attribute.equals(this));
                throw new IllegalArgumentException("Ability ID cannot be duplicate");
            }
        }

        attributeRegistry.add(this);
    }

    /**
     * A static function that retrieves an attribute by its ID
     * @param id The ID of the attribute
     * @return The attribute with the given ID, or null if no attribute with the given ID exists
     */
    @Nullable
    public static Attribute byId(String id) {
        for (Attribute attribute :
                attributeRegistry) {
            if (attribute.id.equalsIgnoreCase(id)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * apply the attribute to the ItemStack
     * MUST set the ItemMeta before and after
     * @param item the item to apply
     * @param modifier the modifier to apply
     * @return the modified ItemStack (may be the same ItemStack in some cases, other times it may be a clone)
     */
    public abstract ItemStack apply(ItemStack item, AttributeModifier modifier);
    public abstract ItemStack remove(ItemStack item, AttributeModifier modifier);

    public String getId() {
        return id;
    }

    public TranslatableText getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public AttributeSource getSource() {
        return source;
    }

    public Function<Double, String> getValueFormatter() {
        return valueFormatter;
    }

    public enum AttributeSource {
        AURELIUM, MINECRAFT, EVOCRAFT
    }
}
