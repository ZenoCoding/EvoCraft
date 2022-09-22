package me.zenox.superitems.item.basicitems;


import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

import static me.zenox.superitems.item.ItemRegistry.BURNING_ASHES;
import static me.zenox.superitems.item.ItemRegistry.PURIFIED_MAGMA_DISTILLATE;

public class DarkSkull extends ComplexItem {

    public DarkSkull() {
        super("dark_skull", Rarity.RARE, Type.MISC, Material.WITHER_SKELETON_SKULL, Map.of());

        List<String> lore = List.of(ChatColor.GRAY + "" + ChatColor.ITALIC + "Smoldering hot.");
        this.getMeta().setLore(lore);
    }
}