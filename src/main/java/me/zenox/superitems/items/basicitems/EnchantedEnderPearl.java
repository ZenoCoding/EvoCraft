package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.items.BasicItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class EnchantedEnderPearl extends BasicItem {

    public EnchantedEnderPearl() {
        super("Enchanted Ender Pearl", "ender_pearl_compact_1", Rarity.COMMON, Type.MISC, Material.ENDER_PEARL, Map.of());

        this.getMeta().addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("EEE", "EEE", "EEE");
        recipe.setIngredient('E', Material.ENDER_PEARL);
        return List.of(recipe);
    }
}
