package me.zenox.superitems.items.armoritems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.items.ArmorItem;
import me.zenox.superitems.items.basicitems.EnchantedIronBlock;
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

public class SilverChesplate extends ArmorItem {
    public SilverChesplate() {
        super("Silver Chestplate", "silver_chestplate", Rarity.RARE, Type.CHESTPLATE, Material.IRON_CHESTPLATE, Map.of(Stats.STRENGTH, 2d, Stats.HEALTH, 2d, Stats.WISDOM, 2d, Stats.REGENERATION, 2d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Knight's Attire");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

        this.getMeta().addEnchant(Enchantment.WATER_WORKER, 1, true);


        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));


        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);

    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("F F", "SKS", "SSS");
        recipe.setIngredient('K', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        recipe.setIngredient('F', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        return List.of(recipe);
    }
}
