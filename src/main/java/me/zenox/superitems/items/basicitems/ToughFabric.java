package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.RAVAGER_SKIN;

public class ToughFabric extends ComplexItem {

    public ToughFabric() {
        super("Tough Fabric", "ravager_skin_2", Rarity.UNCOMMON, Type.MISC, Material.LEATHER, Map.of());
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.getMeta().setLore(List.of(ChatColor.GRAY + "Sewn together with blood, sweat and tears."));
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("SSS", "SSS", "SSS");
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(RAVAGER_SKIN.getItemStack(1)));
        return List.of(recipe);
    }
}
