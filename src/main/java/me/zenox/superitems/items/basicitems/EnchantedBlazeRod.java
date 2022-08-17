package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.ComplexItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;

import java.util.List;
import java.util.Map;


public class EnchantedBlazeRod extends ComplexItem {

    public EnchantedBlazeRod() {
        super("Enchanted Blaze Rod", "enchanted_blaze_rod", ComplexItem.Rarity.COMMON, ComplexItem.Type.MISC, Material.BLAZE_ROD, Map.of());

        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("BBB", "BBB", "BBB");
        recipe.setIngredient('B', Material.BLAZE_ROD);
        ShapelessRecipe recipe2 = new ShapelessRecipe(new NamespacedKey(SuperItems.getPlugin(), "enchanted_blaze_rod_to_normal"), new ItemStack(Material.BLAZE_ROD, 9));
        recipe2.addIngredient(new RecipeChoice.ExactChoice(this.getItemStack(1)));
        return List.of(recipe, recipe2);
    }
}
