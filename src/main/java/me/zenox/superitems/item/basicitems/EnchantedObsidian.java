package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantedObsidian extends ComplexItem {

    public EnchantedObsidian() {
        super("enchanted_obsidian", Rarity.COMMON, Type.MISC, Material.OBSIDIAN, Map.of());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Stronger than bedrock.");
        this.getMeta().setLore(lore););
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("OOO", "OOO", "OOO");
        recipe.setIngredient('O', Material.OBSIDIAN);
        ShapelessRecipe recipe2 = new ShapelessRecipe(new NamespacedKey(SuperItems.getPlugin(), "enchanted_obsidian_to_normal"), new ItemStack(Material.OBSIDIAN, 9));
        recipe2.addIngredient(new RecipeChoice.ExactChoice(this.getItemStack(1)));
        return List.of(recipe, recipe2);
    }
}