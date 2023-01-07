package me.zenox.superitems.events;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.abilities.Ability;
import me.zenox.superitems.enchant.ComplexEnchantment;
import org.bukkit.event.*;
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
        // If the event is not in either of the registered event lists, return
        if(Ability.registeredEvents.stream().filter(aClass -> aClass.isInstance(event)).count() != 0){
            for (Ability ability : Ability.registeredAbilities){
                ability.useAbility(event);
            }
        }

        if(ComplexEnchantment.registeredEvents.stream().filter(aClass -> aClass.isInstance(event)).count() != 0){
            for (ComplexEnchantment enchantment : ComplexEnchantment.getExecutableEnchants()){
                enchantment.useEnchant(event);
            }
        }
    }



}
