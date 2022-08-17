package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.items.ComplexItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class EnchantedMagmaBlock extends ComplexItem {

    public EnchantedMagmaBlock() {

        super("Enchanted Magma Block", "enchanted_magma_block", Rarity.COMMON, Type.MISC, Material.MAGMA_BLOCK, Map.of());

        this.getMeta().addEnchant(Enchantment.ARROW_INFINITE, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("MMM", "MMM", "MMM");
        recipe.setIngredient('M', Material.MAGMA_BLOCK);
        return List.of(recipe);
    }
}