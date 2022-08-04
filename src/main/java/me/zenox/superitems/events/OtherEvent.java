package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class OtherEvent implements Listener {

    private final SuperItems plugin;

    public OtherEvent(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void projectileExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("dmgEnv");
        if (!values.isEmpty() && values.get(0).asBoolean() == false) {
            if (entity instanceof Explosive) {
                Explosive explosive = (Explosive) entity;
                e.setCancelled(true);
                entity.getWorld().createExplosion(entity.getLocation(), explosive.getYield(), false, false, entity instanceof Projectile ? (Entity) ((Projectile) entity).getShooter() : entity);
            }
        }

        List<MetadataValue> kbValues = entity.getMetadata("knockback");
        if (!kbValues.isEmpty()) {
            for (Entity nearbyEntity : entity.getLocation().getWorld().getNearbyEntities(entity.getLocation(), 3, 3, 3)) {
                nearbyEntity.setVelocity(nearbyEntity.getVelocity().add(nearbyEntity.getLocation().toVector().subtract(entity.getLocation().add(0, -0.3, 0).toVector()).normalize().multiply(kbValues.get(0).asInt())));
            }
        }

    }

    @EventHandler
    public void fallingBlockLand(EntityChangeBlockEvent e) {
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("temporary");
        if (!values.isEmpty() && values.get(0).asBoolean() == true) {
            entity.remove();
            e.setCancelled(true);
        }
    }


}
