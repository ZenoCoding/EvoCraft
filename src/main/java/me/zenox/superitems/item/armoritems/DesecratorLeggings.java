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
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.zenox.superitems.item.ItemRegistry.DESECRATOR_SCALE;
import static me.zenox.superitems.item.ItemRegistry.KEVLAR;

public class DesecratorLeggings extends ComplexItem {
    public DesecratorLeggings() {
        super("desecrator_leggings", Rarity.EPIC, Type.LEGGINGS, Material.LEATHER_LEGGINGS, Map.of(Stats.STRENGTH, 5d, Stats.HEALTH, 5d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Raw power.");
        this.getMeta().setLore(lore);
        ((LeatherArmorMeta) this.getMeta()).setColor(Color.SILVER);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", -0.04, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.LEGS));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("SKS", "S S", "S S");
        recipe.setIngredient('K', new RecipeChoice.ExactChoice(KEVLAR.getItemStack(1)));
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(DESECRATOR_SCALE.getItemStack(1)));
        return List.of(recipe);
    }
}
