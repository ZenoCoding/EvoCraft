package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.SoulRift;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.ABSOLUTE_ENDER_PEARL;
import static me.zenox.superitems.items.ItemRegistry.ENCHANTED_BLAZE_ROD;

public class SoulCrystal extends ComplexItem {
    public SoulCrystal() {
        super("Soul Crystal", "soul_crystal", Rarity.LEGENDARY, Type.DEPLOYABLE, Material.END_CRYSTAL, Map.of(), List.of(new SoulRift()));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "The void-Sphere of Life was an anti-magic device, which could absorb the");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "special form of magic in the player. It could not be destroyed by magic");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "weapons. The soul crystal was left by an evil spirit of a mighty elf in that");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "sphere. When you wore the orb, your magic power would be raised, while");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "you could control people and objects with you. The soul crystal had a");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "maximum energy level of 3. There are a few Dark elves whose souls were");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "shattered by the sphere, and they tried to absorb the magic from its");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "remains, but they are too weak and starved and died.");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "");
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Generated at: https://app.inferkit.com/demo");

        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));

        recipe.shape("BEB", "ECE", "BEB");
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(ENCHANTED_BLAZE_ROD.getItemStack(1)));
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(ABSOLUTE_ENDER_PEARL.getItemStack(1)));
        recipe.setIngredient('C', Material.END_CRYSTAL);
        return List.of(recipe);
    }
}
