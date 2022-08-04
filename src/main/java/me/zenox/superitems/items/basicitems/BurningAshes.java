package me.zenox.superitems.items.basicitems;


import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.items.BasicItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class BurningAshes extends BasicItem {

    public BurningAshes() {
        super("Burning Ashes", "burning_ashes", Rarity.UNCOMMON, Type.MISC, Material.GUNPOWDER, Map.of());
        getMeta().addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    @Override
    public List<Recipe> getRecipes(List<BasicItem> registeredItems) {
        return super.getRecipes(registeredItems);
    }
}
