package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.SuperItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerUseItemEvent implements Listener {

    private SuperItems plugin;

    public PlayerUseItemEvent(SuperItems plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void useEvent(PlayerInteractEvent e){
        if(e.getItem() == null) return;
        SuperItem superItem = plugin.registry.getSuperItemFromItemStack(e.getItem());
        if(superItem != null){
            superItem.getAbility().runExecutable(e);
        }
    }


}
