package me.zenox.superitems.enchant;

import me.zenox.superitems.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class DamagedByEntityEnchantment extends ComplexEnchantment{

    private static List<EntityDamageEvent.DamageCause> validCauses = List.of(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.PROJECTILE);

    public DamagedByEntityEnchantment(EnchantmentSettings settings) {
        super(settings, EntityDamageByEntityEvent.class);
    }

    @Override
    boolean checkEvent(Event e) {
        return (e instanceof EntityDamageByEntityEvent event && event.getEntity() instanceof Player && event.getEntity() != event.getDamager() && !Util.isInvulnerable(event.getEntity()) && validCauses.contains(event.getCause()));
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((Player) ((EntityDamageByEntityEvent) e).getEntity());
    }
}
