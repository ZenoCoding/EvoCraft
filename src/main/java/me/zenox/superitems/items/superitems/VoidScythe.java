package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.ObsidilithScytheAbility;
import me.zenox.superitems.items.abilities.VoidScytheAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class VoidScythe extends ComplexItem {
    public VoidScythe() {
        super("Void Scythe", "void_scythe", Rarity.LEGENDARY, Type.MISC, Material.WOODEN_HOE, Map.of(), List.of(new VoidScytheAbility()));

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "tycho make lore");

        this.getMeta().setLore(lore);
        this.getMeta().isUnbreakable();

    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
