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
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.zenox.superitems.items.ItemRegistry.*;

public class VoidCloak extends ArmorItem {
    public VoidCloak() {
        super("Void Cloak", "void_chestplate", Rarity.EPIC, Type.CHESTPLATE, Material.LEATHER_CHESTPLATE, Map.of(Stats.STRENGTH, 3d, Stats.HEALTH, 5d, Stats.WISDOM, 100d, Stats.REGENERATION, 5d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "An ultimate source of wisdom, bringing great power to the wearer");
        this.getMeta().setLore(lore);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.TEAL);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));


        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);

    }

    @Override
    public List<Recipe> getRecipes() {
//        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
//        recipe.shape("F F", "SKS", "SSS");
//        recipe.setIngredient('K', new RecipeChoice.ExactChoice(KEVLAR.getItemStack(1)));
//        recipe.setIngredient('F', new RecipeChoice.ExactChoice(TOUGH_FABRIC.getItemStack(1)));
//        recipe.setIngredient('S', new RecipeChoice.ExactChoice(DESECRATOR_SCALE.getItemStack(1)));
        return List.of();
    }
}
