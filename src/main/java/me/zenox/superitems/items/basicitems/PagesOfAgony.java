package me.zenox.superitems.items.basicitems;

import me.zenox.superitems.items.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;

public class PagesOfAgony extends ComplexItem {

    public PagesOfAgony() {
        super("Pages of Agony", "pages_of_agony", Rarity.COMMON, Type.MISC, Material.PAPER, Map.of());
        this.getMeta().setLore(List.of(ChatColor.GRAY + "Painful to the touch."));
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
}
