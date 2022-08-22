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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

<<<<<<<< HEAD:src/main/java/me/zenox/superitems/items/armoritems/VoidBoots.java
import static me.zenox.superitems.items.ItemRegistry.DESECRATOR_SCALE;
import static me.zenox.superitems.items.ItemRegistry.DESECRATOR_TOE;

public class VoidBoots extends ArmorItem {
    public VoidBoots() {
        super("Boots Of the Void Lord", "void_boots", Rarity.EPIC, Type.BOOTS, Material.LEATHER_BOOTS, Map.of(Stats.STRENGTH, 2d, Stats.HEALTH, 5d, Stats.WISDOM, 30d, Stats.REGENERATION, 5d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Who the heck is this \"Void Lord\" guy?");
========
public class FlamingBoots extends ArmorItem {
    public FlamingBoots() {
        super("Flaming Boots", "flaming_boots", Rarity.EPIC, Type.BOOTS, Material.LEATHER_BOOTS, Map.of(Stats.STRENGTH, 8d, Stats.HEALTH, 5d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Raw power.");
>>>>>>>> d5fd419ea4f0063f3bc088d1cf9b580aa446e384:src/main/java/me/zenox/superitems/items/armoritems/FlamingBoots.java
        this.getMeta().setLore(lore);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.ORANGE);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
<<<<<<<< HEAD:src/main/java/me/zenox/superitems/items/armoritems/VoidBoots.java
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));


========
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:movement_speed", 0.05, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.FEET));
>>>>>>>> d5fd419ea4f0063f3bc088d1cf9b580aa446e384:src/main/java/me/zenox/superitems/items/armoritems/FlamingBoots.java
        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

    @Override
    public List<Recipe> getRecipes() {
<<<<<<<< HEAD:src/main/java/me/zenox/superitems/items/armoritems/VoidBoots.java
//        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
//        recipe.shape("   ", "S S", "T T");
//        recipe.setIngredient('T', new RecipeChoice.ExactChoice(DESECRATOR_TOE.getItemStack(1)));
//        recipe.setIngredient('S', new RecipeChoice.ExactChoice(DESECRATOR_SCALE.getItemStack(1)));
========
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
>>>>>>>> d5fd419ea4f0063f3bc088d1cf9b580aa446e384:src/main/java/me/zenox/superitems/items/armoritems/FlamingBoots.java
        return List.of();
    }
}
