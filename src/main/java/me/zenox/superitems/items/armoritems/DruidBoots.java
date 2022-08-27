package me.zenox.superitems.items.armoritems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.items.ArmorItem;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DruidBoots extends ArmorItem {
    public DruidBoots() {
        super("Druid Boots", "druid_boots", Rarity.RARE, Type.BOOTS, Material.LEATHER_BOOTS, Map.of(Stats.STRENGTH, 3d, Stats.HEALTH, 20d, Stats.WISDOM, 10d, Stats.REGENERATION, 30d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Who would want mud in their shoes???");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        this.getMeta().addEnchant(Enchantment.THORNS, 1, true);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.OLIVE);
        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
