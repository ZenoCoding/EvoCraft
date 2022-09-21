package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class DesecratorClaw extends ComplexItem {

    public DesecratorClaw() {
        super("desecrator_claw", Rarity.EPIC, Type.MISC, Material.DAMAGED_ANVIL, Map.of());
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.getMeta().setLore(List.of(ChatColor.GRAY + "Sharper than a knife."));
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
