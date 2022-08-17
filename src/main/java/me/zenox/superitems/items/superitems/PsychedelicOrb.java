package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.Psychic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.ABSOLUTE_ENDER_PEARL;
import static me.zenox.superitems.items.ItemRegistry.HYPER_CRUX;

public class PsychedelicOrb extends ComplexItem {

    public PsychedelicOrb() {
        super("Psychedelic Orb", "psychedelic_orb", ComplexItem.Rarity.RARE, ComplexItem.Type.MISC, Material.ENDER_PEARL, Map.of(Stats.WISDOM, 1000d), List.of(new Psychic()));
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.getMeta().setLore(List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "In order for one to know the world around it, they must know about thyself.",
                "", ChatColor.GRAY + "Voodoo magic!"));
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapelessRecipe recipe = new ShapelessRecipe(this.getKey(), this.getItemStack(1));
        recipe.addIngredient(new RecipeChoice.ExactChoice(HYPER_CRUX.getItemStack(1)));
        recipe.addIngredient(new RecipeChoice.ExactChoice(ABSOLUTE_ENDER_PEARL.getItemStack(1)));
        return List.of(recipe);
    }
}
