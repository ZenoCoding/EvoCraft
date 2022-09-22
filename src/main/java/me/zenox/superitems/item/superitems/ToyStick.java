package me.zenox.superitems.item.superitems;

import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.MagicMissile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class ToyStick extends ComplexItem {
    public ToyStick() {
        super( "magic_toy_stick", Rarity.EPIC, Type.SUPERITEM, Material.STICK, Map.of(), List.of(new MagicMissile(6, true)));

        List<String> lore = List.of(ChatColor.RED + "" + ChatColor.ITALIC + "Magical.");

        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

}
