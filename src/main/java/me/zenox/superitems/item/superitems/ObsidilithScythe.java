package me.zenox.superitems.item.superitems;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.abilities.AbilityRegistry;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.item.ItemRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ObsidilithScythe extends ComplexItem {
    public ObsidilithScythe() {
        super("obsidilith_scythe", Rarity.LEGENDARY, Type.MISC, Material.NETHERITE_HOE, Map.of(), List.of(AbilityRegistry.OBSIDIAN_SHARD));

        List<String> lore = List.of(ChatColor.GRAY + "Forged from thousands of compacted obsidian,",
                ChatColor.GRAY + "and joined together with corrupted souls.");

        this.getMeta().setLore(lore);
        this.getMeta().setUnbreakable(true);

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "superitems:attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", -2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        this.getMeta().setAttributeModifiers(attributeMap);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), new ComplexItemStack(this).getItem());
        recipe.shape("MCC", "MDM", "WDW");
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.CORRUPT_OBSIDIAN).getItem()));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(new ComplexItemStack(ItemRegistry.MOLTEN_POWDER).getItem()));
        recipe.setIngredient('D', Material.DEBUG_STICK);
        recipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        return List.of(recipe);
    }
}
