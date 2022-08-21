package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.MagicMissile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class DevAxe extends ComplexItem {
    public DevAxe() {
        super("Dev Axe", "dev_axe", Rarity.VERY_SPECIAL, Type.SUPERITEM, Material.WOODEN_PICKAXE, Map.of(), List.of(new MagicMissile(5, false)));

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "Built Diffrent");

        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 10, true);
        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addEnchant(Enchantment.KNOCKBACK, 42, true);
        this.getMeta().addEnchant(Enchantment.SILK_TOUCH, 1, true);
        this.getMeta().addEnchant(Enchantment.DIG_SPEED, 100, true);
        
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
