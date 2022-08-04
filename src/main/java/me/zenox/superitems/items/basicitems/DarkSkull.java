package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class DarkSkull extends BasicItem {

    public DarkSkull() {
        super("Dark Skull", "dark_skull", Rarity.RARE, Type.MISC, Material.WITHER_SKELETON_SKULL, Map.of());

        List<String> lore = List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "Smoldering hot.");
        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("MWM", "AWA", "AWA");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("burning_ashes").getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("purified_magma_distillate").getItemStack(1)));
        recipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        return List.of(recipe);
    }
}