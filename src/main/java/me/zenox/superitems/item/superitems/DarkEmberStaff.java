package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.EmberAttune;
import me.zenox.superitems.item.abilities.EmberShoot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.*;

public class DarkEmberStaff extends ComplexItem {
    public DarkEmberStaff() {
        super("dark_ember_staff", true, Rarity.RARE, Type.STAFF, Material.BLAZE_ROD, Map.of(Stats.WISDOM, 100d, Stats.STRENGTH, -100d), List.of(new EmberAttune(), new EmberShoot()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A fiery rod, taken from " + ChatColor.RED + "Blaziel" + ChatColor.GRAY + " and imbued with darkness.");
        lore.add(ChatColor.GRAY + "Draws power from the user's " + ChatColor.AQUA + "mana.");
        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("RWR", "PSP", "MPM");
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(FIERY_EMBER_STAFF.getItemStack(1)));
        recipe.setIngredient('P', new RecipeChoice.ExactChoice(MOLTEN_POWDER.getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(PURIFIED_MAGMA_DISTILLATE.getItemStack(1)));
        recipe.setIngredient('W', new RecipeChoice.ExactChoice(DARK_SKULL.getItemStack(1)));
        recipe.setIngredient('R', Material.WITHER_ROSE);
        return List.of(recipe);
    }
}
