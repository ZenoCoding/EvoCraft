package me.zenox.superitems.attribute;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.attribute.types.AureliumAttribute;
import me.zenox.superitems.attribute.types.MinecraftAttribute;
import org.bukkit.ChatColor;

public class AttributeRegistry {
    public static final Attribute MAX_HEALTH = new MinecraftAttribute("mc-max_health", ChatColor.RED, org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
    public static final Attribute FOLLOW_RANGE = new MinecraftAttribute("mc-follow_range", ChatColor.GREEN, org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE);
    public static final Attribute KNOCKBACK_RESISTANCE = new MinecraftAttribute("mc-knockback_resistance", ChatColor.DARK_GRAY, org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE);
    public static final Attribute MOVEMENT_SPEED = new MinecraftAttribute("mc-movement_speed", ChatColor.WHITE, org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED);
    public static final Attribute FLIGHT_SPEED = new MinecraftAttribute("mc-flight_speed", ChatColor.WHITE, org.bukkit.attribute.Attribute.GENERIC_FLYING_SPEED);
    public static final Attribute ATTACK_DAMAGE = new MinecraftAttribute("mc-attack_damage", ChatColor.RED, org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
    public static final Attribute ATTACK_KNOCKBACK = new MinecraftAttribute("mc-attack_knockback", ChatColor.AQUA, org.bukkit.attribute.Attribute.GENERIC_ATTACK_KNOCKBACK);
    public static final Attribute ATTACK_SPEED = new MinecraftAttribute("mc-attack_speed", ChatColor.AQUA, org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
    public static final Attribute ARMOR = new MinecraftAttribute("mc-armor", ChatColor.GOLD, org.bukkit.attribute.Attribute.GENERIC_ARMOR);
    public static final Attribute ARMOR_TOUGHNESS = new MinecraftAttribute("mc-armor_toughness", ChatColor.DARK_GREEN, org.bukkit.attribute.Attribute.GENERIC_ARMOR_TOUGHNESS);
    public static final Attribute LUCK = new MinecraftAttribute("mc-luck", ChatColor.GREEN, org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);

    public static final Attribute STRENGTH = new AureliumAttribute("as-strength", ChatColor.RED, Stats.STRENGTH);
    public static final Attribute HEALTH = new AureliumAttribute("as-health", ChatColor.RED, Stats.HEALTH);
    public static final Attribute REGENERATION = new AureliumAttribute("as-regeneration", ChatColor.YELLOW, Stats.REGENERATION);
    public static final Attribute AS_LUCK = new AureliumAttribute("as-luck", ChatColor.GREEN, Stats.LUCK);
    public static final Attribute WISDOM = new AureliumAttribute("as-wisdom", ChatColor.BLUE, Stats.WISDOM);
    public static final Attribute TOUGHNESS = new AureliumAttribute("as-toughness", ChatColor.DARK_PURPLE, Stats.TOUGHNESS);

}
