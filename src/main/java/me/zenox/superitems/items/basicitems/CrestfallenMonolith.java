package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.ComplexItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zenox.superitems.items.ItemRegistry.COMPACT_OBSIDIAN;
import static me.zenox.superitems.items.ItemRegistry.CORRUPT_PEARL;

public class CrestfallenMonolith extends ComplexItem implements Listener {

    public CrestfallenMonolith() {
        super("Crestfallen Monolith", "crestfallen_monolith", Rarity.RARE, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().setLore((List.of(ChatColor.GRAY + "Please get some help.")));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIzOWMwZTUzZTc5ZTdlYmQ0ZGI2YzZkMDk2YzlkOWExNjBjZmYyNzgyMmMwNzdmYjhmNWQ0NTk2OWNjNDk3MiJ9fX0=");

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }
}
