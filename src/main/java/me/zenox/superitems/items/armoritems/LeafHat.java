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
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LeafHat extends ArmorItem {
    public LeafHat() {
        super("Leaf Hat ", "leaf_hat", Rarity.RARE, Type.HELMET, Material.PLAYER_HEAD, Map.of(Stats.STRENGTH, 1d, Stats.HEALTH, 10d, Stats.REGENERATION, 20d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Nice hat that blocks sunlight." );
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);


        ((LeatherArmorMeta) this.getMeta()).setColor(Color.RED);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));



        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRlZTUxNGIwMGNhN2UzMDlkYWQ0ZmQ4MzRjYWU2ZmY4ZTRlYmY1ZWY4ZGE3MzEyZGEzMjc3MTRmNjViYTg2ZiJ9fX0=");

    }

    @Override
    public List<Recipe> getRecipes() {
//        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
          return List.of();
    }
}
