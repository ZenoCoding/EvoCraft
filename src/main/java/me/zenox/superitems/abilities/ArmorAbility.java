package me.zenox.superitems.abilities;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.function.Consumer;

/**
 * General Armor Ability with no class
 */
public class ArmorAbility extends Ability {

    public ArmorAbility(String id, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.MAIN_HAND);
    }

    public ArmorAbility(String id, int manaCost, double cooldown, Slot slot) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, slot);
    }

    public ArmorAbility(String id, int manaCost, double cooldown, Consumer<Event> exectuable) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.MAIN_HAND, exectuable);
    }

    @Override
    public boolean checkEvent(Event e) {
        return super.checkEvent(e);
    }

}
