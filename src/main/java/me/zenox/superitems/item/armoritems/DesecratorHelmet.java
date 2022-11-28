package me.zenox.superitems.item.armoritems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.zenox.superitems.item.ItemRegistry.*;

public class DesecratorHelmet extends ComplexItem {
    public DesecratorHelmet() {
        super("desecrator_helmet", Rarity.EPIC, Type.HELMET, Material.PLAYER_HEAD, Map.of(Stats.STRENGTH, 25d, Stats.HEALTH, 25d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Raw power.");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        this.getMeta().addEnchant(Enchantment.THORNS, 3, true);
        this.getMeta().addEnchant(Enchantment.WATER_WORKER, 1, true);
        this.getMeta().addEnchant(Enchantment.OXYGEN, 3, true);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", -0.025, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HEAD));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMjUwNGJjZGM1MTU5MThlZGNlM2Y3ZDY4YTQ1YTgyYWYyYWE3NTg2OGJiMmI2MmI3OTE4NzdkNmQyYjRkYyJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("KCK", "SGS", "   ");
        recipe.setIngredient('K', new RecipeChoice.ExactChoice(KEVLAR.getItemStack(1)));
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(CRULEN_SHARD.getItemStack(1)));
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(DESECRATOR_SCALE.getItemStack(1)));
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(PYTHEMION_GEM.getItemStack(1)));
        return List.of(recipe);
    }
}
