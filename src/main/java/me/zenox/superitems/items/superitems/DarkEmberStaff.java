package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.abilities.EmberAttune;
import me.zenox.superitems.items.abilities.EmberShoot;
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

public class DarkEmberStaff extends SuperItem {
    public DarkEmberStaff() {
        super("Dark Ember Staff", "dark_ember_staff", Rarity.RARE, Type.STAFF, Material.BLAZE_ROD, true, 1, Map.of(Stats.WISDOM, 125d, Stats.STRENGTH, -75d), List.of(new EmberAttune(), new EmberShoot()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A fiery rod, taken from " + ChatColor.RED + "Blaziel" + ChatColor.GRAY + " and imbued with darkness.");
        lore.add(ChatColor.GRAY + "Draws power from the user's " + ChatColor.AQUA + "mana.");
        this.getMeta().setLore(lore);

        PersistentDataContainer dataContainer = this.getMeta().getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(SuperItems.getPlugin(), "ember_attunement"), PersistentDataType.STRING, "blazeborn");
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("RWR", "PSP", "MPM");
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("fiery_ember_staff").getItemStack(1)));
        recipe.setIngredient('P', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("molten_powder").getItemStack(1)));
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("purified_magma_distillate").getItemStack(1)));
        recipe.setIngredient('W', new RecipeChoice.ExactChoice(SuperItems.getPlugin().registry.getBasicItemFromId("dark_skull").getItemStack(1)));
        recipe.setIngredient('R', Material.WITHER_ROSE);
        return List.of(recipe);
    }
}
