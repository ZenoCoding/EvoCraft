package me.zenox.evocraft.enchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamagedEnchantment extends ComplexEnchantment{

    public DamagedEnchantment(EnchantmentSettings settings) {
        super(settings, EntityDamageEvent.class);
    }

    @Override
    boolean checkEvent(Event e) {
        return (e instanceof EntityDamageEvent event && event.getEntity() instanceof Player);
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((Player) ((EntityDamageEvent) e).getEntity());
    }
}
