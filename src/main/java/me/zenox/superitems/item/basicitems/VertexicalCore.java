package me.zenox.superitems.items.basicitems;


import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class VertexicalCore extends ComplexItem {

    public VertexicalCore() {
        super("vertexical_core", Rarity.EPIC, Type.MISC, Material.PLAYER_HEAD, Map.of());

        this.getMeta().setLore(List.of(ChatColor.GRAY + "Convex Polygamous Spacial Geometry.",
            ChatColor.GRAY + "Yes, that's " + ChatColor.ITALIC + "technically" + ChatColor.GRAY + " English"));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWYwMjMwNTExYjg0NGNmM2FmZjBjZWRiNDRjMTMyNDI3OTlkMzMxNTIyMzVmMTdjZWU1NzQ2NTE4NzhlZDVkMCJ9fX0=");
    }

}
