package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.abilities.AbilityRegistry;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VertexicalBlade extends ComplexItem implements Listener {
    public VertexicalBlade() {
        super("vertex_blade", true, Rarity.RARE, Type.MISC, Material.AMETHYST_SHARD, Map.of(Stats.STRENGTH, 100d, Stats.WISDOM, 300d), List.of(AbilityRegistry.VERTEX_ABILITY));

        List<String> lore = List.of(ChatColor.GRAY + "Using " + ChatColor.LIGHT_PURPLE + "vertexical math and convex geometry,",
                ChatColor.GRAY + "this blade bends the fabric of " + ChatColor.DARK_GRAY + "space-time.",
                "",
                ChatColor.GOLD + "Item Ability: Atom-split Alignment",
                ChatColor.GRAY + "For every " + ChatColor.RED + "consecutive" + ChatColor.GRAY + " hit on an entity,",
                ChatColor.GRAY + "construct a point on a " + ChatColor.GOLD + "Tesseract," + ChatColor.GRAY + " increasing ",
                ChatColor.RED + "damage" + ChatColor.GRAY + " against that enemy.");

        this.getMeta().setLore(lore);
        this.getMeta().isUnbreakable();

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "superitems:attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", -2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        this.getMeta().setAttributeModifiers(attributeMap);

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 7, true);
        this.getMeta().addEnchant(Enchantment.LOOT_BONUS_MOBS, 5, true);

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

}
