package me.zenox.superitems.attribute;

import me.zenox.superitems.attribute.types.MinecraftAttribute;
import org.bukkit.ChatColor;

public class AttributeRegistry {
    public static final Attribute ATTACK_DAMAGE = new MinecraftAttribute("attack_damage", ChatColor.RED, org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
}
