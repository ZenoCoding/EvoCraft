package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.Crucify;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.*;

public class CrucifiedAmulet extends ComplexItem {
    public CrucifiedAmulet() {
        super( "crucified_amulet", ComplexItem.Rarity.RARE, Type.MISC, Material.PLAYER_HEAD, Map.of(Stats.WISDOM, 5d), List.of(new Crucify()));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Inevitable demise.");
        this.getMeta().setLore(lore);
        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWUwZGRmNDEyYzYwYTdiZWY4NGE3YjhlNmZjYTcxNGQ0ODgyYWYxMTE4ZTk3NDAwYzg4ZDExYmE1YTk0N2RjYSJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("STS", "AHA", "STS");
        recipe.setIngredient('H', new RecipeChoice.ExactChoice(HYPER_CRUX.getItemStack(1)));
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(BURNING_ASHES.getItemStack(1)));
        recipe.setIngredient('T', new RecipeChoice.ExactChoice(TORMENTED_SOUL.getItemStack(1)));
        recipe.setIngredient('S', Material.SOUL_SAND);
        return List.of(recipe);
    }
}
