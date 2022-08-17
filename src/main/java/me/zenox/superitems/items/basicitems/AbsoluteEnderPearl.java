package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.COMPACTED_ENDER_PEARL;

public class AbsoluteEnderPearl extends ComplexItem {

    public AbsoluteEnderPearl() {
        super("Absolute Ender Pearl", "ender_pearl_compact_3", Rarity.RARE, Type.MISC, Material.ENDER_PEARL, Map.of());

        this.getMeta().addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.getMeta().setLore(List.of(ChatColor.GRAY + "Contains the souls of 729 Endermen."));
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("EEE", "EEE", "EEE");
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(COMPACTED_ENDER_PEARL.getItemStack(1)));
        return List.of(recipe);
    }
}
