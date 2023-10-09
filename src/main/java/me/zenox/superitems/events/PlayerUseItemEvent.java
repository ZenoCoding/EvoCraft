package me.zenox.superitems.events;

import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.abilities.AttackAbility;
import me.zenox.superitems.abilities.ItemAbility;
import me.zenox.superitems.enchant.ComplexEnchantment;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerUseItemEvent implements Listener {

    private final SuperItems plugin;
    private final Map<Class<? extends Event>, List<Ability>> eventToAbilitiesMap = new HashMap<>();
    private final Map<Class<? extends Event>, List<ComplexEnchantment>> eventToEnchantmentsMap = new HashMap<>();


    public PlayerUseItemEvent(SuperItems plugin) {
        this.plugin = plugin;

        // Populate eventToAbilitiesMap based on registered abilities
        for (Ability ability : Ability.registeredAbilities) {
            Class<? extends Event> eventType = ability.getEventType(); // Assuming you have a method to get the event type
            eventToAbilitiesMap
                    .computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(ability);
        }

        for (ComplexEnchantment enchantment : ComplexEnchantment.getRegisteredEnchants()) {
            Class<? extends Event> eventType = enchantment.getEventType();
            eventToEnchantmentsMap
                    .computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(enchantment);
        }

        RegisteredListener registeredListener = new RegisteredListener(this, (listener, event) -> useEvent(event), EventPriority.NORMAL, plugin, false);
        for (HandlerList handler : HandlerList.getHandlerLists())
            handler.register(registeredListener);
    }

    @EventHandler
    public void useEvent(Event event) {
        // PERFORMANCE ISSUE: This is called for every event, and every single ability is called.
        List<Ability> relevantAbilities = eventToAbilitiesMap.get(event.getClass());
        if (relevantAbilities != null) {
            for (Ability ability : relevantAbilities) {
                ability.useAbility(event);
            }
        }

        List<ComplexEnchantment> relevantEnchantments = eventToEnchantmentsMap.get(event.getClass());
        if (relevantEnchantments != null) {
            for (ComplexEnchantment enchantment : relevantEnchantments) {
                enchantment.useEnchant(event);
            }
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
        for (Ability ability : complexItem.getAbilities()) {
            if (ability instanceof ItemAbility) ability.useAbility(event);
        }
    }


}
