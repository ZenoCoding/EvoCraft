package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.Centralize;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.CRULEN_SHARD;

public class TotemPole extends ComplexItem {
    public TotemPole() {
        super("Totem Pole", "totem_pole", Rarity.RARE, Type.DEPLOYABLE, Material.PLAYER_HEAD, Map.of(Stats.WISDOM, 5d), List.of(new Centralize(false, 45)));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Centralized power.");
        this.getMeta().setLore(lore);
        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4MDkxMjhhMGU1YTQ0YzJlMzk1MzJlNmJiYzY4MjUyY2I4YzlkNWVjZDI0NmU1OTY1MDc3YzE0N2M3OTVlNyJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("TCT", "DND", "DND");
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(CRULEN_SHARD.getItemStack(1)));
        recipe.setIngredient('D', Material.DEBUG_STICK);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        return List.of(recipe);
    }
}
