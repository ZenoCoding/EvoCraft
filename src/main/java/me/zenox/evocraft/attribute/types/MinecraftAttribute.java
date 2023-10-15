package me.zenox.evocraft.attribute.types;

import me.zenox.evocraft.attribute.Attribute;
import me.zenox.evocraft.attribute.AttributeModifier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Function;

public class MinecraftAttribute extends Attribute {

    private final org.bukkit.attribute.Attribute mcAttribute;

    public MinecraftAttribute(String id, ChatColor color, org.bukkit.attribute.Attribute mcAttribute) {
        this(id, color, mcAttribute, value -> (value > 0 ? "+" : "") + value);
    }

    public MinecraftAttribute(String id, ChatColor color, org.bukkit.attribute.Attribute mcAttribute, Function<Double, String> valueFormatter) {
        super(id, color, AttributeSource.MINECRAFT, valueFormatter);
        this.mcAttribute = mcAttribute;
    }

    @Override
    public ItemStack apply(ItemStack item, AttributeModifier modifier) {
        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(mcAttribute, new org.bukkit.attribute.AttributeModifier(modifier.getUuid(), modifier.getName(), modifier.getValue(), modifier.getOperation(), modifier.getSlot().getMcSlot()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack remove(ItemStack item, AttributeModifier modifier) {
        ItemMeta meta = item.getItemMeta();
        meta.removeAttributeModifier(((MinecraftAttribute) modifier.getAttribute()).getMcAttribute());
        item.setItemMeta(meta);
        return item;
    }

    public org.bukkit.attribute.Attribute getMcAttribute() {
        return mcAttribute;
    }
}
