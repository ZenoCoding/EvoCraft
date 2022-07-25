package me.zenox.superitems.items;

import me.zenox.superitems.util.Executable;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface BasicItemInterface {
    String getName();
    String getId();
    BasicItem.Rarity getRarity();
    SuperItem.Type getType();
    ItemMeta getItemMeta();
    Material getMaterial();
}
