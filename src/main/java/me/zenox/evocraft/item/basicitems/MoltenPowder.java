package me.zenox.evocraft.item.basicitems;

import me.zenox.evocraft.item.ComplexItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.evocraft.item.ItemRegistry.*;

public class MoltenPowder extends ComplexItem {

    public MoltenPowder() {
        super("molten_powder", Rarity.RARE, Type.MISC, Material.BLAZE_POWDER, Map.of());

        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("ABM", "BBB", "MBA");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(BURNING_ASHES.getItemStack(1)));
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(ENCHANTED_BLAZE_ROD.getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(PURIFIED_MAGMA_DISTILLATE.getItemStack(1)));
        return List.of(recipe);
    }
}
