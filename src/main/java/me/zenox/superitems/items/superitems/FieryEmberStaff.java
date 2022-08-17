package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.EmberShootSmall;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.*;

public class FieryEmberStaff extends ComplexItem {
    public FieryEmberStaff() {
        super("Fiery Ember Staff", "fiery_ember_staff", Rarity.UNCOMMON, Type.STAFF, Material.BLAZE_ROD, Map.of(Stats.WISDOM, 50d, Stats.STRENGTH, -50d), List.of(new EmberShootSmall()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);//////////
        this.getMeta().setLore(List.of(ChatColor.GRAY + "A fiery rod, taken from " + ChatColor.RED + "Blaziel" + ChatColor.GRAY + " himself."));
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("FFF", "APA", "MPM");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(BURNING_ASHES.getItemStack(1)));
        recipe.setIngredient('P', new RecipeChoice.ExactChoice(MOLTEN_POWDER.getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(PURIFIED_MAGMA_DISTILLATE.getItemStack(1)));
        recipe.setIngredient('F', Material.FIRE_CHARGE);
        return List.of(recipe);
    }
}
