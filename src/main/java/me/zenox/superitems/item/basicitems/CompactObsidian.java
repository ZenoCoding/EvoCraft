package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.items.ComplexItem;
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

public class CompactObsidian extends ComplexItem implements Listener {

    public CompactObsidian() {
        super("compact_obsidian", Rarity.COMMON, Type.MISC, Material.PLAYER_HEAD, Map.of());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Breaking the laws of physics, it's gravitational attraction warps space-time.");
        this.getMeta().setLore(lore);

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE5MjZiZmE5Y2FmOGJjYTkwNjkyNzgwOTc4YjVjNzRkNzEzZTg2NWY1YmRkMzc5MjA5N2IxODc5OTk3ZTU1NyJ9fX0=");

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("OOO", "OOO", "OOO");
        recipe.setIngredient('O', new RecipeChoice.ExactChoice(ItemRegistry.ENCHANTED_OBSIDIAN.getItemStack(1)));
        ShapelessRecipe recipe2 = new ShapelessRecipe(new NamespacedKey(SuperItems.getPlugin(), "compact_obsidian_to_ench"), ENCHANTED_OBSIDIAN.getItemStack(9));
        recipe2.addIngredient(new RecipeChoice.ExactChoice(this.getItemStack(1)));
        return List.of(recipe, recipe2);
    }
}
