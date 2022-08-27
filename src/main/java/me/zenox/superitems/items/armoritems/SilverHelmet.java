package me.zenox.superitems.items.armoritems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.items.ArmorItem;
import me.zenox.superitems.items.basicitems.EnchantedIronBlock;
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

import static me.zenox.superitems.items.ItemRegistry.ENCHANTED_IRON_BLOCK;

public class SilverHelmet extends ArmorItem {
    public SilverHelmet() {
        super("Silver Helmets", "silver_helmet", Rarity.RARE, Type.HELMET, Material.IRON_HELMET, Map.of(Stats.STRENGTH, 1d, Stats.HEALTH, 2d, Stats.WISDOM, 3d, Stats.REGENERATION, 0d));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Knight's Attire");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

        this.getMeta().addEnchant(Enchantment.WATER_WORKER, 1, true);
        this.getMeta().addEnchant(Enchantment.OXYGEN, 3, true);


        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "superitems:armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        attributeMap.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "superitems:armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));


        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setUnbreakable(true);

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc3ZDRhMjA2ZDc3NTdmNDc5ZjMzMmVjMWEyYmJiZWU1N2NlZjk3NTY4ZGQ4OGRmODFmNDg2NGFlZTdkM2Q5OCJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("KCK", "SGS", "   ");
        recipe.setIngredient('K', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(2)));
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        return List.of(recipe);
    }
}
