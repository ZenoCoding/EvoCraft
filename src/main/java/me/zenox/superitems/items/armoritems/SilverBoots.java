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


public class SilverBoots extends ArmorItem {
    public SilverBoots() {
        super("Silver Boots", "silver_boots", Rarity.LEGENDARY, Type.BOOTS, Material.IRON_BOOTS, Map.of(Stats.STRENGTH, 100d, Stats.HEALTH, 100d, Stats.WISDOM, 100d, Stats.REGENERATION, 100d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Stolen shoes from the finest of battalion soldiers.");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 20, true);
        this.getMeta().addEnchant(Enchantment.THORNS, 20, true);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("   ", "S S", "T T");
        recipe.setIngredient('T', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(10)));
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(10)));
        return List.of(recipe);
    }
}
