package me.zenox.superitems.attribute.types;

import me.zenox.superitems.attribute.Attribute;
import me.zenox.superitems.attribute.AttributeModifier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;

public class MinecraftAttribute extends Attribute {

    private final org.bukkit.attribute.Attribute mcAttribute;

    public MinecraftAttribute(String id, ChatColor color, org.bukkit.attribute.Attribute mcAttribute) {
        super(id, color, null, AttributeSource.MINECRAFT);
        try {
            Field f = getClass().getSuperclass().getDeclaredField("lore");
            f.setAccessible(true);
            f.set(this, generateLore());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.mcAttribute = mcAttribute;
    }

    private String generateLore(){
        return this.getName() + ": " + this.getColor() + Attribute.VALUE_PLACEHOLDER;
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
