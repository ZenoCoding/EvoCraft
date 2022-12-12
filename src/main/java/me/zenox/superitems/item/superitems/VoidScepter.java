package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.abilities.AbilityRegistry;
import me.zenox.superitems.item.ComplexItem;
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

public class VoidScepter extends ComplexItem {
    public VoidScepter() {
        super("void_scepter", Rarity.EPIC, Type.STAFF, Material.NETHERITE_SHOVEL, Map.of(Stats.WISDOM, 50d), List.of(AbilityRegistry.VOID_WARP, AbilityRegistry.VOIDULAR_RECALL));

        List<String> lore = List.of(ChatColor.GRAY + "Forged from the souls of corrupted endermen.");


        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "superitems:attack_damage", 15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", -3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        this.getMeta().setAttributeModifiers(attributeMap);
        this.getMeta().setLore(lore);
        this.getMeta().setUnbreakable(true);

    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1))
                .shape("CVC", "NDN", "CDC")
                .setIngredient('C', new RecipeChoice.ExactChoice(ItemRegistry.CORRUPT_PEARL.getItemStack(1)))
                .setIngredient('V', new RecipeChoice.ExactChoice(ItemRegistry.VOID_STONE.getItemStack(1)))
                .setIngredient('D', Material.DEBUG_STICK);
        return List.of(recipe);
    }

}
