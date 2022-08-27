package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.BaneofNoobsAbility;
import me.zenox.superitems.items.abilities.Speed100;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaneofNoobs extends ComplexItem {
    public BaneofNoobs() {
        super("Bane of Noobs", "bane_of_hamilton", Rarity.UNCOMMON, Type.SWORD, Material.STONE_SWORD, Map.of(Stats.WISDOM, -100d), List.of(new BaneofNoobsAbility()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Wow, your such a bully.");
        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
