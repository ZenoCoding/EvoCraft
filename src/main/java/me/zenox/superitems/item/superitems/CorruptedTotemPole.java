package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.abilities.Centralize;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CorruptedTotemPole extends ComplexItem {
    public CorruptedTotemPole() {
        super("corrupted_totem_pole", true, Rarity.RARE, Type.DEPLOYABLE, Material.PLAYER_HEAD, Map.of(Stats.STRENGTH, 5d), List.of(new Centralize(true, 60)));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "With great power, comes " + ChatColor.LIGHT_PURPLE + "corruption.");
        this.getMeta().setLore(lore);
        this.setSkullURL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk3YzI4NmI2ZDE2MjM5YTcxZmYxNjc0OTQ0MTZhZDk0MDcxNzIwNTEwY2Y4YTgyYWIxZjQ1MWZmNGE5MDkxNiJ9fX0=");
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(this.getKey(), this.getItemStack(1));
        return List.of();
    }
}
