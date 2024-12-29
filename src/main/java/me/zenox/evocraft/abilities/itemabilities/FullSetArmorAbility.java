package me.zenox.evocraft.abilities.itemabilities;

import me.zenox.evocraft.Slot;
import me.zenox.evocraft.abilities.AbilitySettings;
import me.zenox.evocraft.abilities.EventAbility;
import me.zenox.evocraft.item.ComplexItem;
import me.zenox.evocraft.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * General Armor Ability with no class
 */
public abstract class FullSetArmorAbility<T extends Event> extends EventAbility<T> {
    public FullSetArmorAbility(AbilitySettings settings) {
        super(settings);
    }

    public FullSetArmorAbility(AbilitySettings settings, TriConsumer<T, Player, ItemStack> executable) {
        super(settings, executable);
    }

    public FullSetArmorAbility(String id, int manaCost, double cooldown, TriConsumer<T, Player, ItemStack> executable) {
        super(id, manaCost, cooldown, Slot.ARMOR, executable);
    }

    @Override
    protected boolean checkEvent(T e) {
        return checkEventExec(e) && Slot.ARMOR.item(getPlayerOfEvent(e)).stream().allMatch(itemStack -> {
            try {
                return ComplexItem.of(itemStack).getAbilities().contains(this);
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
    protected abstract boolean checkEventExec(T e);

}
