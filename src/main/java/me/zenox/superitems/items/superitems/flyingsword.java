package me.zenox.superitems.items.superitems;

import me.zenox.superitems.items.ComplexItem;
import me.zenox.superitems.items.abilities.FlyingSwordAbility;
import me.zenox.superitems.items.abilities.MagicMissile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class flyingsword extends ComplexItem {
    public flyingsword() {
        super("Flying Sword", "Flying Sword", Rarity.LEGENDARY, Type.SUPERITEM, Material.IRON_SWORD, Map.of(), List.of(new MagicMissile(1, false), new FlyingSwordAbility()));

        List<String> lore = List.of(ChatColor.GOLD + "" + ChatColor.ITALIC + "A flying Sword");

        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 10, true);
        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addEnchant(Enchantment.KNOCKBACK, 42, true);
        this.getMeta().addEnchant(Enchantment.SILK_TOUCH, 1, true);


        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.getMeta().isUnbreakable();
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
