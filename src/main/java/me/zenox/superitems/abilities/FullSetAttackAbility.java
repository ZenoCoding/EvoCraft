package me.zenox.superitems.abilities;

import me.zenox.superitems.util.TriConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FullSetAttackAbility extends FullSetArmorAbility<EntityDamageByEntityEvent> {

    public FullSetAttackAbility(AbilitySettings settings) {
        super(settings);
    }

    public FullSetAttackAbility(AbilitySettings settings, TriConsumer<EntityDamageByEntityEvent, Player, ItemStack> executable) {
        super(settings, executable);
    }

    public FullSetAttackAbility(String id, int manaCost, double cooldown, TriConsumer<EntityDamageByEntityEvent, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, exectuable);
    }

    @Override
    protected boolean checkEventExec(EntityDamageByEntityEvent e) {
        return e.getDamager() instanceof Player && !Util.isInvulnerable(e.getEntity());
    }

    @Override
    Player getPlayerOfEvent(EntityDamageByEntityEvent e) {
        return ((Player) e.getDamager());
    }

    @Override
    List<ItemStack> getItem(Player p, EntityDamageByEntityEvent e) {
        return this.getSlot().item(p);
    }

    // Static ability executables
    public static void testFullSetAbility(EntityDamageByEntityEvent e, Player p, ItemStack item){
        Util.sendMessage(p, "Woo! You used a full set ability!");
    }


    public static void roaringFlameAbility(Event e, Player p, ItemStack item) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        event.getEntity().setFireTicks(event.getEntity().getFireTicks() + 100);
    }
}
