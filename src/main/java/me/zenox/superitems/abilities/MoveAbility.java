package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MoveAbility extends Ability {

    public MoveAbility(String id, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, PlayerMoveEvent.class, Slot.ARMOR);
    }

    public MoveAbility(String id, int manaCost, double cooldown, Slot slot) {
        super(id, manaCost, cooldown, PlayerMoveEvent.class, slot);
    }

    public MoveAbility(String id, int manaCost, double cooldown, TriConsumer<Event, Player, ItemStack> executable) {
        super(id, manaCost, cooldown, PlayerMoveEvent.class, Slot.ARMOR, executable);
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

    @Override
    public void useAbility(Event e) {
        super.useAbility(e);
    }

    // Static ability executables
    public static void lavaGliderAbility(Event e, Player p, ItemStack item) {
        PlayerMoveEvent event = ((PlayerMoveEvent) e);
        // If the player is in lava
        if (p.getWorld().getBlockAt(p.getLocation()).getType().equals(Material.LAVA)
            || p.getWorld().getBlockAt(p.getLocation().subtract(0, 1, 0)).getType().equals(Material.LAVA))
            p.setWalkSpeed(0.3f);
        else p.setWalkSpeed(0.2f);

    }

}
