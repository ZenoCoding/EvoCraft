package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.Tarhelm;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.*;

public class TormentedBlade extends ComplexItem {
    public TormentedBlade() {
        super("tormented_blade", Rarity.LEGENDARY, Type.AXE, Material.IRON_AXE, Map.of(Stats.STRENGTH, 50d), List.of(new Tarhelm()));

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Created from a tortured soul.");
        this.getMeta().setLore(lore);
    }
}
