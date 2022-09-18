package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class PythemionGem extends ComplexItem {

    public PythemionGem() {
        super("Pythemion Gem", "pythemion_gem", Rarity.EPIC, Type.MISC, Material.EMERALD, Map.of());
        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 15, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.getMeta().setLore(List.of(ChatColor.GRAY + "Really fricking hot."));
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
