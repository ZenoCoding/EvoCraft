package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.BasicItem;
import me.zenox.superitems.items.SuperItem;
import me.zenox.superitems.items.abilities.ItemAbility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerUseItemEvent implements Listener {

    private final SuperItems plugin;

    public PlayerUseItemEvent(SuperItems plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void useEvent(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        BasicItem basicItem = plugin.registry.getBasicItemFromItemStack(e.getItem());
        if (basicItem instanceof SuperItem) {
            SuperItem superItem = (SuperItem) basicItem;
            if (superItem != null) {
                for (ItemAbility ability : superItem.getAbilities()) {
                    ability.useAbility(e);
                }
            }
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (item == null) return;
        BasicItem basicItem = plugin.registry.getBasicItemFromItemStack(item);
        if (basicItem != null) {
            e.setCancelled(true);
        }
    }


}
