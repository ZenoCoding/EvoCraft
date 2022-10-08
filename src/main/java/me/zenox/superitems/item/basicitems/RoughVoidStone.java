package me.zenox.superitems.item.basicitems;


import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class RoughVoidStone extends ComplexItem {

    public RoughVoidStone() {
        super("rough_void_stone", Rarity.EPIC, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.getMeta().setLore(List.of(ChatColor.GRAY + "Who knows what it's really for?"));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQzZWNlNTdmODYyZmUxNGIxZmVkY2Y4Zjc5NmNmMzE3NGU5MGNhY2ZiMDIwYTIxYjU5OWY4NDE2N2E2NjVkNyJ9fX0=");
    }
}
