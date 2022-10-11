package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

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

    public ArmorAbility(String id, int manaCost, double cooldown, TriConsumer<Event, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.MAIN_HAND, exectuable);
    }

    @Override
    public boolean checkEvent(Event e) {
        return true;
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return null;
    }

    @Override
    ItemStack getItem(Player p) {
        return null;
    }

}
