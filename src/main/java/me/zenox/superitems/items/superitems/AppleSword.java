package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.ItemRegistry;
import me.zenox.superitems.items.abilities.Tarhelm;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static me.zenox.superitems.items.ItemRegistry.*;

public class AppleSword extends ComplexItem {
    public AppleSword() {



        super("Apple Sword", "apple_sword", Rarity.LEGENDARY, Type.SWORD, Material.ENCHANTED_GOLDEN_APPLE, Map.of(Stats.STRENGTH, 50d, Stats.REGENERATION, 30d, Stats.TOUGHNESS, 20d);

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 16, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Damage based on your experience.");
        this.getMeta().setLore(lore);



    }


    @Override
    public List<Recipe> getRecipes() {
//        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
//        recipe.shape("ITI", "MBI", " B ");
//        recipe.setIngredient('B', new RecipeChoice.ExactChoice(ENCHANTED_BLAZE_ROD.getItemStack(1)));
//        recipe.setIngredient('M', new RecipeChoice.ExactChoice(MOLTEN_POWDER.getItemStack(1)));
//        recipe.setIngredient('T', new RecipeChoice.ExactChoice(TORMENTED_SOUL.getItemStack(1)));
//        recipe.setIngredient('I', new RecipeChoice.ExactChoice(ENCHANTED_IRON_BLOCK.getItemStack(1)));
        return List.of();
    }
}
