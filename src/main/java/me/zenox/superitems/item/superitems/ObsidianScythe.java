package me.zenox.superitems.item.superitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class ObsidianScythe extends ComplexItem {
    public ObsidianScythe() {
        super("obsidian_scythe", Rarity.LEGENDARY, Type.MISC, Material.NETHERITE_HOE, Map.of(), List.of());

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "The Densest of All Compact Obisidians Combined to Make this Terror...");

        this.getMeta().setLore(lore);
        this.getMeta().isUnbreakable();

    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
