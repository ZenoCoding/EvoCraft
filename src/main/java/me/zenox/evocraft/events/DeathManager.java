package me.zenox.evocraft.events;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathManager implements Listener {

    private EvoCraft plugin;

    public DeathManager(EvoCraft plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Util.sendMessage(event.getEntity(), ChatColor.RED + "You died and lost %s coins!".formatted(ChatColor.GOLD + "" + Math.ceil(EvoCraft.getEconomy().getBalance(event.getEntity())*0.2*100)/100));
        EvoCraft.getEconomy().withdrawPlayer(event.getEntity(), EvoCraft.getEconomy().getBalance(event.getEntity()) * 0.1);
        event.getEntity().playSound(event.getEntity().getLocation(), "entity.player.death_coins", 1, 1);
        event.getEntity().playSound(event.getEntity().getLocation(), "entity.player.retro_death", 1, 1);
    }

}
