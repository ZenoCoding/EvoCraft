package me.zenox.evocraft.item.basicitems;

import me.zenox.evocraft.item.ComplexItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GardenerSapling extends ComplexItem {
    public GardenerSapling() {
        super("gardener_sapling", Rarity.VERY_SPECIAL, Type.MISC, Material.OAK_SAPLING, Map.of());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A sapling, that can when cared for");
        lore.add(ChatColor.GRAY + "can grow into something beautiful.");
        lore.add("");
        lore.add(ChatColor.GRAY + "A thanks to the beta testers who helped along the way.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Issued to: " + ChatColor.YELLOW + "%s");
        lore.add(ChatColor.GRAY + "Issued From: " + ChatColor.YELLOW + "%s");
        lore.add(ChatColor.GRAY + "Date Issued: " + ChatColor.YELLOW + "%s");
        this.getMeta().setLore(lore);
        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        this.getMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
}
