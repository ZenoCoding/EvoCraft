package me.zenox.superitems.item.armoritems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlamingChestplate extends ComplexItem {
    public FlamingChestplate() {
        super("flaming_chestplate", Rarity.EPIC, Type.CHESTPLATE, Material.LEATHER_CHESTPLATE, Map.of(Stats.STRENGTH, 10d, Stats.HEALTH, 2d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Raw power.");
        this.getMeta().setLore(lore);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.SILVER);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", 0.1, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.CHEST));
        attributeMap.put(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:movement_speed", 0.1, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.CHEST));
        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

}
