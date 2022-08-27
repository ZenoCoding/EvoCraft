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

import static me.zenox.superitems.items.ItemRegistry.ENCHANTED_IRON_BLOCK;

public class SilverLeggings extends ArmorItem {
    public SilverLeggings() {
        super("Silver Leggings", "silver_leggings", Rarity.RARE, Type.LEGGINGS, Material.IRON_LEGGINGS, Map.of(Stats.STRENGTH, 2d, Stats.HEALTH, 2d, Stats.WISDOM, 1d, Stats.REGENERATION, 1d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Standard Kinght's Attire");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

        this.getMeta().addEnchant(Enchantment.WATER_WORKER, 1, true);


        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));


        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("SKS", "S S", "S S");
        recipe.setIngredient('K', new RecipeChoice.ExactChoice(EnchantedIronBlock.getItemStack(1)));
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        return List.of(recipe);
    }
}
