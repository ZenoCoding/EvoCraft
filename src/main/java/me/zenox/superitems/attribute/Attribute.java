package me.zenox.superitems.attribute;

import me.zenox.superitems.data.TranslatableText;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Attribute implements Serializable {
    public final static String VALUE_PLACEHOLDER = "{value}";
    public final static List<Attribute> attributeRegistry = new ArrayList<>();

    public final String id;
    public final TranslatableText name;
    public final ChatColor color;
    public final String lore;
    private final AttributeSource source;

    public Attribute(String id, ChatColor color, String lore, AttributeSource source) {
        this.id = id;
        this.name = new TranslatableText(TranslatableText.Type.ATTRIBUTE + "-" + id);
        this.color = color;
        this.lore = lore;
        this.source = source;

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

    public String getLore() {
        return lore;
    }

    public AttributeSource getSource() {
        return source;
    }

    public enum AttributeSource {
        AURELIUM, MINECRAFT, SUPERITEMS
    }
}
