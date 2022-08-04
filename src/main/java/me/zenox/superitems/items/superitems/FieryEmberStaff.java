package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.abilities.EmberShootSmall;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FieryEmberStaff extends SuperItem {
    public FieryEmberStaff() {
        super("Fiery Ember Staff", "fiery_ember_staff", Rarity.UNCOMMON, Type.STAFF, Material.BLAZE_ROD, true, 1, Map.of(Stats.WISDOM, 50d, Stats.STRENGTH, -50d), List.of(new EmberShootSmall()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A fiery rod, taken from " + ChatColor.RED + "Blaziel" + ChatColor.GRAY + " himself.");
        this.getMeta().setLore(lore);

        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("FFF", "APA", "MPM");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("burning_ashes").getItemStack(1)));
        recipe.setIngredient('P', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("molten_powder").getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("purified_magma_distillate").getItemStack(1)));
        recipe.setIngredient('F', Material.FIRE_CHARGE);
        return List.of(recipe);
    }
}
