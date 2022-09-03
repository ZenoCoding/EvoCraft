package me.zenox.superitems.items.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.Tarhelm;
import me.zenox.superitems.items.abilities.Transcendence;
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

import static me.zenox.superitems.items.ItemRegistry.*;

public class DimensionalJournal extends ComplexItem {
    public DimensionalJournal() {
        super("Dimensional Journal", "dimension_journal", Rarity.EPIC, Type.MISC, Material.PLAYER_HEAD, Map.of(Stats.WISDOM, 100d), List.of(new Transcendence()));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNkMDgxMTY4Y2E4NjYwNGZjZjM3ODAwMzQ4Y2MxNzJjZTc0MDczOWRiM2NjMDgwZjA3ZjFhN2ZiZGZmZjQ4OSJ9fX0=");

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "... and the Multiverse of Madness™️");
        this.getMeta().setLore(lore);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

}
