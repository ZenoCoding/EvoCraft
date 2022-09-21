package me.zenox.superitems.item.basicitems;

import me.zenox.superitems.item.ComplexItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class HyperCrux extends ComplexItem {

    public HyperCrux() {
        super("hyper_crux", Rarity.RARE, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg4MWM4NzY0ZmJmOTNiMjMxNWNhMTEzNTczZmE3OTlmNGNmZGMwY2E4NjhjODM0MDUyNTVhN2Q1NTZhNzM5ZiJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
