package me.zenox.evocraft.item.basicitems;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.item.ComplexItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RavagerSkin extends ComplexItem implements Listener {

    public RavagerSkin() {
        super("ravager_skin", Rarity.COMMON, Type.MISC, Material.LEATHER, Map.of());
        this.getMeta().setLore(List.of(ChatColor.GRAY + "Rough, isn't it?"));

        Bukkit.getPluginManager().registerEvents(this, EvoCraft.getPlugin());
    }

    @EventHandler
    public void onRavagerDeath(EntityDeathEvent e) {
        if (e.getEntity().getType().equals(EntityType.RAVAGER))
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), this.getItemStack(new Random().nextInt(3)));
    }
}
