package me.zenox.superitems.item.armoritems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.item.ArmorItem;
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

public class VoidLeggings extends ArmorItem {
    public VoidLeggings() {
        super("void_leggings", Rarity.EPIC, Type.LEGGINGS, Material.LEATHER_LEGGINGS, Map.of(Stats.STRENGTH, 3d, Stats.HEALTH, 5d, Stats.WISDOM, 75d, Stats.REGENERATION, 5d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Whispering secrets of magic.");
        this.getMeta().setLore(lore);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.TEAL);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));


        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }
}
