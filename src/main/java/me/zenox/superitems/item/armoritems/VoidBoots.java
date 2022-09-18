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
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class VoidBoots extends ArmorItem {
    public VoidBoots() {
        super("Boots Of the Void Lord", "void_boots", Rarity.EPIC, Type.BOOTS, Material.LEATHER_BOOTS, Map.of(Stats.STRENGTH, 2d, Stats.HEALTH, 5d, Stats.WISDOM, 30d, Stats.REGENERATION, 5d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Who the heck is this \"Void Lord\" guy?");
        this.getMeta().setLore(lore);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.ORANGE);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
