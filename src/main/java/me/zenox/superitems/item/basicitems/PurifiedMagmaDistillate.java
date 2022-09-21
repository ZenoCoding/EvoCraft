package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.ENCHANTED_MAGMA_BLOCK;

public class PurifiedMagmaDistillate extends ComplexItem {

    public PurifiedMagmaDistillate() {
        super("purified_magma_distillate", Rarity.UNCOMMON, Type.MISC, Material.MAGMA_CREAM, Map.of());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Burning hot.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Rumors are that a single drop could power a city for generations.");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 10, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("TML", "MMM", "LMT");
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(ENCHANTED_MAGMA_BLOCK.getItemStack(1)));
        recipe.setIngredient('L', Material.LAVA_BUCKET);
        recipe.setIngredient('T', Material.GHAST_TEAR);
        return List.of(recipe);
    }
}
