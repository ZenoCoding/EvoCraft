package me.zenox.evocraft.enchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Objects;

/**
 * Basic enchantment that does not contain an executable.
 */
public class BasicEnchantment extends ComplexEnchantment{

    public BasicEnchantment(EnchantmentSettings settings) {
        super(settings, null);
        if(Objects.nonNull(settings.getExecutable())) throw new IllegalArgumentException("Basic enchantments cannot have an executable.");
    }

    @Override
    boolean checkEvent(Event e) {
        return false;
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return null;
    }

}
