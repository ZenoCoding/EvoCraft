package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class MoveAbility extends Ability {

    public MoveAbility(String id, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.ARMOR);
    }

    public MoveAbility(String id, int manaCost, double cooldown, Slot slot) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, slot);
    }

    public MoveAbility(String id, int manaCost, double cooldown, TriConsumer<Event, Player, ItemStack> executable) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.ARMOR, executable);
    }

    @Override
    protected boolean checkEvent(Event e) {
        return e instanceof PlayerMoveEvent;
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((PlayerMoveEvent) e).getPlayer();
    }

    @Override
    List<ItemStack> getItem(Player p, Event e) {
        return this.getSlot().item(p);
    }


    public static void lavaGliderAbility(Event e, Player p, ItemStack item) {
        PlayerMoveEvent event = ((PlayerMoveEvent) e);
        // If the player is in lava
        if (p.getWorld().getBlockAt(p.getLocation()).getType().equals(Material.LAVA)
            || p.getWorld().getBlockAt(p.getLocation().subtract(0, 1, 0)).getType().equals(Material.LAVA)){
            // Get the player's current motion vector and multiply by 1.5x
            Vector newMotion = event.getTo().toVector().subtract(event.getFrom().toVector()).multiply(1.5);
            // Add it back to the old location
            event.setTo(event.getFrom().add(newMotion.toLocation(p.getWorld())));
        }
    }
}
