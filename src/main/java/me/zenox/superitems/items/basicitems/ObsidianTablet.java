package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class ObsidianTablet extends ComplexItem {

    public ObsidianTablet() {
        super("Obsidian Tablet", "obsidian_tablet", Rarity.EPIC, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

    this.getMeta().setLore(List.of(ChatColor.GRAY + "Inscribed with the secrets of magic from all around the universe."));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWExNTlkOWFjMmZkYjE2NDg1OGI2MzUwZTAzYzE5MjRmMTdlNWFhODYxMWEzNDdkNTViNmI4OTgyMGZhZjA5NCJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
