package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class MoltenPowder extends BasicItem {

    public MoltenPowder() {
        super("Molten Powder", "molten_powder", Rarity.RARE, Type.MISC, Material.BLAZE_POWDER, Map.of());

        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("ABM", "BBB", "MBA");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("burning_ashes").getItemStack(1)));
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("enchanted_blaze_rod").getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("purified_magma_distillate").getItemStack(1)));
        return List.of(recipe);
    }
}
