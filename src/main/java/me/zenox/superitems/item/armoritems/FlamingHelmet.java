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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlamingHelmet extends ComplexItem {
    public FlamingHelmet() {
        super("flaming_helmet", Rarity.EPIC, Type.HELMET, Material.LEATHER_HELMET, Map.of(Stats.STRENGTH, 10d, Stats.HEALTH, 2d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Seriously hot.");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
        this.getMeta().addEnchant(Enchantment.THORNS, 3, true);
        this.getMeta().addEnchant(Enchantment.WATER_WORKER, 1, true);
        this.getMeta().addEnchant(Enchantment.OXYGEN, 3, true);
        this.getMeta().addEnchant(Enchantment.MENDING, 1, true);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.RED);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:movement_speed", 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HEAD));


        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);


    }

    @Override
    public List<Recipe> getRecipes() {
//        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
