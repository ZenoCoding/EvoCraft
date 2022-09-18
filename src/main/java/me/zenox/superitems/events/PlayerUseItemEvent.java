package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.item.abilities.Ability;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

public class PlayerUseItemEvent implements Listener {

    private final SuperItems plugin;

    public PlayerUseItemEvent(SuperItems plugin) {
        this.plugin = plugin;
        RegisteredListener registeredListener = new RegisteredListener(this, (listener, event) -> useEvent(event), EventPriority.NORMAL, plugin, false);
        for (HandlerList handler : HandlerList.getHandlerLists())
            handler.register(registeredListener);
    }

    @EventHandler
    public void useEvent(Event event) {
        if (!(event instanceof PlayerEvent)){
            return;
        }

        if(event instanceof PlayerInteractEvent){
            interact(((PlayerInteractEvent) event));
            return;
        }

        PlayerEvent e = (PlayerEvent) event;
        for (ItemStack item : e.getPlayer().getInventory().getContents()) {
            if (item == null) continue;
            ComplexItem complexItem = ItemRegistry.getBasicItemFromItemStack(item);
            if (complexItem == null) continue;
            for (Ability ability : complexItem.getAbilities()) {
                if (!ability.getSlot().evaluate(e.getPlayer().getInventory(), ability)) continue;
                ability.useAbility(e);
            }
        }
    }

    private boolean interact(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        if (item == null) return false;
        ComplexItem complexItem = ItemRegistry.getBasicItemFromItemStack(item);
        if (complexItem == null) return false;
        for (Ability ability : complexItem.getAbilities()) {
            ability.useAbility(event);
        }
        return false;
    }


}
