package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.MagicMissile;
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

public class BuffSword extends ComplexItem {
    public BuffSword() {
        super("buff_sword", "buff_sword", Rarity.RARE, Type.SWORD, Material.DIAMOND_SWORD, Map.of(), Map.of(Stats.STRENGTH, 60d);


        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "Very Buff Sword");

        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addEnchant(Enchantment.KNOCKBACK, 2, true);


        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.getMeta().isUnbreakable();
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        recipe.shape("STS", "AHA", "   ");
        recipe.setIngredient('H', new RecipeChoice.ExactChoice(HYPER_CRUX.getItemStack(1)));
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));

        recipe.setIngredient('S', Material.SOUL_SAND);
        return super.getRecipes(recipe);
    }
}
