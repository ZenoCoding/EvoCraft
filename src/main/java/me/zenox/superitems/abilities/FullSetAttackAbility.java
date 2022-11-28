package me.zenox.superitems.abilities;

import me.zenox.superitems.util.TriConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FullSetAttackAbility extends FullSetArmorAbility {

    public FullSetAttackAbility(String id, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class);
    }

    public FullSetAttackAbility(String id, int manaCost, double cooldown, TriConsumer<Event, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, exectuable);
    }

    @Override
    protected boolean checkEventExec(Event e) {
        return e instanceof EntityDamageByEntityEvent event && event.getDamager() instanceof Player && !Util.isInvulnerable(event.getEntity());
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((Player) ((EntityDamageByEntityEvent) e).getDamager());
    }

    @Override
    List<ItemStack> getItem(Player p, Event e) {
        return this.getSlot().item(p);
    }

    // Static ability executables
    public static void testFullSetAbility(Event e, Player p, ItemStack item){
        Util.sendMessage(p, "Woo! You used a full set ability!");
    }


    public static void roaringFlameAbility(Event e, Player p, ItemStack item) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        event.getEntity().setFireTicks(event.getEntity().getFireTicks() + 100);
    }
}
