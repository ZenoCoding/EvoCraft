package me.zenox.evocraft.enchant;

import me.zenox.evocraft.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class AttackEnchantment extends ComplexEnchantment{

    private static List<EntityDamageEvent.DamageCause> validCauses = List.of(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.PROJECTILE);

    public AttackEnchantment(EnchantmentSettings settings) {
        super(settings, EntityDamageByEntityEvent.class);
    }

    @Override
    boolean checkEvent(Event e) {
        return (e instanceof EntityDamageByEntityEvent event && event.getDamager() instanceof Player && event.getDamager() != event.getEntity() && !Util.isInvulnerable(event.getEntity()) && validCauses.contains(event.getCause()));
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((Player) ((EntityDamageByEntityEvent) e).getDamager());
    }

}
