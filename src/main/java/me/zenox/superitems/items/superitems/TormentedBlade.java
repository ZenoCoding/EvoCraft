package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.abilities.Tarhelm;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TormentedBlade extends SuperItem {
    public TormentedBlade() {
        super("Tormented Blade", "tormented_blade", Rarity.LEGENDARY, Type.AXE, Material.IRON_AXE, false, 1, Map.of(Stats.STRENGTH, 50d), List.of(new Tarhelm()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Created from a tortured soul.");
        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("ITI", "MBI", " B ");
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("enchanted_blaze_rod").getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("molten_powder").getItemStack(1)));
        recipe.setIngredient('T', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("tormented_soul").getItemStack(1)));
        recipe.setIngredient('I', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("enchanted_iron_block").getItemStack(1)));
        return List.of(recipe);
    }
}
