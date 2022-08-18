package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.ObsidilithScytheAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class ObsidianScythe extends ComplexItem {
    public ObsidianScythe() {
        super("Obsidian Scythe", "obsidian_scythe", Rarity.LEGENDARY, Type.MISC, Material.DIAMOND_HOE, Map.of(), List.of(new ObsidilithScytheAbility()));

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "The Densest of All Compact Obisidians Combined to Make this Terror...");

        this.getMeta().setLore(lore);

    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
