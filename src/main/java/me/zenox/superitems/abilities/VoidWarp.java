package me.zenox.superitems.abilities;

import me.zenox.superitems.SuperItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class VoidWarp extends ItemAbility {
    public VoidWarp() {
        super("void_warp", AbilityAction.RIGHT_CLICK_ALL, 100, 1);

        this.addLineToLore(ChatColor.GRAY + "Creates a rift in space and pulls you through");
        this.addLineToLore(ChatColor.GRAY + "it, teleporting you up to 75 blocks.");

    }

    @Override
    public void runExecutable(Event event, Player p, ItemStack item) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.6f);

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= 20) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.8f);

                    Block endblock = p.getTargetBlockExact(75, FluidCollisionMode.NEVER);
                    Vector dir = p.getLocation().getDirection();
                    if (endblock == null) {
                        p.teleport(p.getLocation().add(dir.normalize().multiply(75)).add(0, 1, 0).setDirection(dir));
                    } else {
                        p.teleport(endblock.getLocation().add(0, 1, 0).setDirection(dir));
                    }
                    cancel();
                }

                if (count % 4 == 0) p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 1.4f);

                for (int i = 0; i < 4; i++) {
                    Location loc = p.getLocation().clone().add(r.nextDouble() - 0.5, r.nextDouble() + 0.1, r.nextDouble() - 0.5);
                    World w = p.getWorld();
                    w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                }
                count++;
            }
        }.runTaskTimer(SuperItems.getPlugin(), 5, 1);
    }
}
