package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.items.ComplexItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class BurningAshes extends ComplexItem {

    public BurningAshes() {
        super("Burning Ashes", "burning_ashes", Rarity.UNCOMMON, Type.MISC, Material.GUNPOWDER, Map.of());
        this.getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
