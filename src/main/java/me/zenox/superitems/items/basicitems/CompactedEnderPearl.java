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

public class CompactedEnderPearl extends BasicItem {

    public CompactedEnderPearl() {
        super("Compacted Ender Pearl", "ender_pearl_compact_2", Rarity.UNCOMMON, Type.MISC, Material.ENDER_PEARL, Map.of());

        this.getMeta().addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("EEE", "EEE", "EEE");
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("ender_pearl_compact_1").getItemStack(1)));
        return List.of(recipe);
    }
}
