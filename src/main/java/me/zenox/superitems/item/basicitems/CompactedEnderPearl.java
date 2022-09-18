package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.ENCHANTED_ENDER_PEARL;

public class CompactedEnderPearl extends ComplexItem {

    public CompactedEnderPearl() {
        super("Compacted Ender Pearl", "ender_pearl_compact_2", Rarity.UNCOMMON, Type.MISC, Material.ENDER_PEARL, Map.of());

        this.getMeta().addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("EEE", "EEE", "EEE");
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(ENCHANTED_ENDER_PEARL.getItemStack(1)));
        return List.of(recipe);
    }
}
