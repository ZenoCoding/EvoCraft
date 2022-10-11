package me.zenox.superitems.events;

import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.abilities.AttackAbility;
import me.zenox.superitems.abilities.ItemAbility;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        for (Ability ability : Ability.registeredAbilities){
            ability.useAbility(event);
        }

//        if (event instanceof EntityDamageByEntityEvent e) {
//            if (e.getDamager() instanceof Player p) {
//                for (Ability ability :
//                        Slot.uniqueEquipped(p)) {
//                    if (ability instanceof AttackAbility) {
//                        ability.useAbility(event);
//                    }
//                }
//            }
//        }
//
//        if (event instanceof PlayerInteractEvent) {
//            interact(((PlayerInteractEvent) event));
//        }
    }

    private void interact(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        ComplexItemStack complexItem = ComplexItemStack.of(item);
        if (complexItem == null) return;
        for (Ability ability : complexItem.getAbilities()) {
            if (ability instanceof ItemAbility) ability.useAbility(event);
        }
    }


}
