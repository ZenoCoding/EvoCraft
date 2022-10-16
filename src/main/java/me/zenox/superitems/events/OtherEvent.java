package me.zenox.superitems.events;

import de.studiocode.inventoryaccess.component.BaseComponentWrapper;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.gui.EnchantingGUI;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ItemRegistry;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class OtherEvent implements Listener {

    private final SuperItems plugin;

    public OtherEvent(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void tileEntityInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            switch(e.getClickedBlock().getType()){
                case ENCHANTING_TABLE -> new SimpleWindow(e.getPlayer(), "Enchantment Table", EnchantingGUI.getGui(e.getPlayer(), e.getClickedBlock()), true, true).show();
                default -> {
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void projectileExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("dmgEnv");
        if (!values.isEmpty() && !values.get(0).asBoolean()) {
            if (entity instanceof Explosive explosive) {
                e.setCancelled(true);
                entity.getWorld().createExplosion(entity.getLocation(), explosive.getYield(), false, false, entity instanceof Projectile ? (Entity) ((Projectile) entity).getShooter() : entity);
            }
        }

        List<MetadataValue> kbValues = entity.getMetadata("knockback");
        if (!kbValues.isEmpty()) {
            for (Entity nearbyEntity : entity.getLocation().getWorld().getNearbyEntities(entity.getLocation(), 3, 3, 3)) {
                if (nearbyEntity.isInvulnerable()) continue;
                nearbyEntity.setVelocity(nearbyEntity.getVelocity().add(nearbyEntity.getLocation().toVector().subtract(entity.getLocation().add(0, -0.3, 0).toVector()).normalize().multiply(kbValues.get(0).asInt())));
            }
        }

    }

    @EventHandler
    public void fallingBlockLand(EntityChangeBlockEvent e) {
        Entity entity = e.getEntity();
        List<MetadataValue> values = entity.getMetadata("temporary");
        if (!values.isEmpty() && values.get(0).asBoolean()) {
            entity.remove();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (item == null) return;
        ComplexItem complexItem = ItemRegistry.byItem(item);
        if (complexItem != null) {
            e.setCancelled(true);
        }
    }


}
