package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.Speed100;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JackassGonzales extends ComplexItem {
    public JackassGonzales() {
        super("speed_sword_2", Rarity.UNCOMMON, Type.SWORD, Material.GOLDEN_SWORD, Map.of(Stats.WISDOM, 15d), List.of(new Speed100(3, 150)));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Light speed! (Lvl.3 Mage Spell)");
        this.getMeta().setLore(lore);
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
