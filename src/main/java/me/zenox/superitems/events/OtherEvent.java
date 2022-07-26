package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.ItemAbility;
import me.zenox.superitems.items.SuperItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class OtherEvent implements Listener {

    private SuperItems plugin;

    public OtherEvent(SuperItems plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void projectileExplode(EntityExplodeEvent e){
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("dmgEnv");
        if (!values.isEmpty() && values.get(0).asBoolean() == false) {
            if(entity instanceof Explosive) {
                Explosive explosive = (Explosive) entity;
                e.setCancelled(true);
                entity.getWorld().createExplosion(entity.getLocation(), explosive.getYield(), false, false);
            }
        }
    }


}
