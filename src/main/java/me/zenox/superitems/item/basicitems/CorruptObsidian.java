package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.SuperItems;
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

public class CorruptObsidian extends ComplexItem {

    public CorruptObsidian() {
        super("corrupt_obsidian", Rarity.UNCOMMON, Type.MISC, Material.PLAYER_HEAD, Map.of());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Spaghettification!");
        this.getMeta().setLore(lore);

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFjODc0NTRmMWVlNTg1YjkwZmRiM2EzZTQwOTUyYTVjMmY4MGMwYTQ5ZGZlYzYyODcwZmRmZjE4Mzk2N2E4NCJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("COO", "ONO", "OOC");
        recipe.setIngredient('O', new RecipeChoice.ExactChoice(COMPACT_OBSIDIAN.getItemStack(1)));
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(CORRUPT_PEARL.getItemStack(1)));
        recipe.setIngredient('N', Material.NETHER_STAR);
        return List.of(recipe);
    }
}