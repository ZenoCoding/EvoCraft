package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.TriConsumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class MoveAbility extends Ability<PlayerMoveEvent> {

    public MoveAbility(AbilitySettings settings) {
        super(settings);
    }

    public MoveAbility(AbilitySettings settings, TriConsumer<PlayerMoveEvent, Player, ItemStack> executable) {
        super(settings, executable);
    }

    public MoveAbility(String id, int manaCost, double cooldown, TriConsumer<PlayerMoveEvent, Player, ItemStack> executable) {
        super(id, manaCost, cooldown, Slot.ARMOR, executable);
    }

    @Override
    protected boolean checkEvent(PlayerMoveEvent e) {
        return e instanceof PlayerMoveEvent;
    }

    @Override
    Player getPlayerOfEvent(PlayerMoveEvent e) {
        return ((PlayerMoveEvent) e).getPlayer();
    }

    @Override
    List<ItemStack> getItem(Player p, PlayerMoveEvent e) {
        return this.getSlot().item(p);
    }

    // Static ability executables
    public static void lavaGliderAbility(PlayerMoveEvent e, Player p, ItemStack item) {
        // If the player is in lava
        if (p.getWorld().getBlockAt(p.getLocation()).getType().equals(Material.LAVA)
            || p.getWorld().getBlockAt(p.getLocation().subtract(0, 1, 0)).getType().equals(Material.LAVA)){
            p.setWalkSpeed(0.5f);

            Random r = new Random();
            for (int i = 0; i < 2; i++) {
                // Spawn a LAVA particle that is randomized but near player's location
                Location loc = p.getLocation();
                loc.add(r.nextDouble() - 0.5d, r.nextDouble() - 0.5d, r.nextDouble() - 0.5d);
                p.getWorld().spawnParticle(Particle.LAVA, loc, 1);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setWalkSpeed(0.2f);
                }
            }.runTaskLater(SuperItems.getPlugin(), 1);
        }
        else p.setWalkSpeed(0.2f);

    }

}
