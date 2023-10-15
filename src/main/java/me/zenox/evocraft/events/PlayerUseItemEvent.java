package me.zenox.evocraft.events;

import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.abilities.Ability;
import me.zenox.evocraft.abilities.ItemAbility;
import me.zenox.evocraft.enchant.ComplexEnchantment;
import me.zenox.evocraft.item.ComplexItemStack;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerUseItemEvent implements Listener {

    private final EvoCraft plugin;
    private final Map<Class<? extends Event>, List<Ability>> eventToAbilitiesMap = new HashMap<>();
    private final Map<Class<? extends Event>, List<ComplexEnchantment>> eventToEnchantmentsMap = new HashMap<>();


    public PlayerUseItemEvent(EvoCraft plugin) {
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
