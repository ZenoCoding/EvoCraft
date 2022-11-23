package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.item.ComplexItemStack;
import me.zenox.superitems.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * General Armor Ability with no class
 */
public abstract class FullSetArmorAbility extends Ability {
    public FullSetArmorAbility(String id, int manaCost, double cooldown, Class<? extends Event> eventType) {
        super(id, manaCost, cooldown, eventType, Slot.ARMOR);
    }

    public FullSetArmorAbility(String id, int manaCost, double cooldown, Class<? extends Event> eventType, TriConsumer<Event, Player, ItemStack> executable) {
        super(id, manaCost, cooldown, eventType, Slot.ARMOR, executable);
    }

    @Override
    protected boolean checkEvent(Event e) {
        return checkEventExec(e) && Slot.ARMOR.item(getPlayerOfEvent(e)).stream().allMatch(itemStack -> {
            try {
                return ComplexItemStack.of(itemStack).getAbilities().contains(this);
            } catch (NullPointerException exception){
                return false;
            }
        });
    }

    /**
     * CheckEvent Executable that can be overridden by the superclass to allow for a more general check to check for the full set having the ability
     * @param e
     * @return
     */
    protected abstract boolean checkEventExec(Event e);

}
