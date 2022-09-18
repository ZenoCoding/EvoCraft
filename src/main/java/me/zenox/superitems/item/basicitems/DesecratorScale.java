package me.zenox.superitems.item.basicitems;


import me.zenox.superitems.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class DesecratorScale extends ComplexItem {

    public DesecratorScale() {
        super("Desecrator Scale", "desecrator_scale", Rarity.RARE, Type.MISC, Material.PLAYER_HEAD, Map.of());
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.getMeta().setLore(List.of(ChatColor.GRAY + "Tougher than nails."));

        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlmMTFjMGQ0OTdmMjI3MDg5Y2JmNjE0NjAxMTA5Y2FmNjE1NDUzOTQwZWY1ZjY0ZWJiMTc3OTU3ZTRmYTZlNSJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        return super.getRecipes();
    }
}
