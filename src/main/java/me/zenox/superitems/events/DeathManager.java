package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathManager implements Listener {

    private SuperItems plugin;

    public DeathManager(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Util.sendMessage(event.getEntity(), ChatColor.RED + "You died and lost %s coins!".formatted(ChatColor.GOLD + "" + Math.ceil(SuperItems.getEconomy().getBalance(event.getEntity())*0.2*100)/100));
        SuperItems.getEconomy().withdrawPlayer(event.getEntity(), SuperItems.getEconomy().getBalance(event.getEntity()) * 0.1);
        event.getEntity().playSound(event.getEntity().getLocation(), "entity.player.death_coins", 1, 1);
        event.getEntity().playSound(event.getEntity().getLocation(), "entity.player.retro_death", 1, 1);
    }

}
