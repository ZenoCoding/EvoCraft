package me.zenox.superitems.item;

import com.archyx.aureliumskills.stats.Stat;
import org.bukkit.Material;

import java.util.Map;

public class ArmorItem extends ComplexItem {


    public ArmorItem(String name, String id, Rarity rarity, Type type, Material material, Map<Stat, Double> stats) {
        super(name, id, rarity, type, material, stats);
    }
}
