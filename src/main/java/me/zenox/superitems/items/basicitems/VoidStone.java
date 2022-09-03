package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.*;

public class VoidStone extends ComplexItem {

    public VoidStone() {
        super("Void Stone", "void_stone", Rarity.LEGENDARY, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

    this.getMeta().setLore(List.of(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC + "Who knows what it's really for?"));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ0ODc1MGJiNWFkYTI5ZGZmNjgyMGRiNjkzM2RjNjJhMGJmNmJkZTcyNzM0MWViN2RkMTg0NTNhMTBkNjQ5MyJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("CKC", "CVC", "CCC");
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(CORRUPT_PEARL.getItemStack(1)));
        recipe.setIngredient('K', new RecipeChoice.ExactChoice(KEVLAR.getItemStack(1)));
        recipe.setIngredient('V', new RecipeChoice.ExactChoice(ROUGH_VOID_STONE.getItemStack(1)));

        return List.of(recipe);
    }
}
