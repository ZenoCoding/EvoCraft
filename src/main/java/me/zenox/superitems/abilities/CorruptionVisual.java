package me.zenox.superitems.abilities;

import me.zenox.superitems.SuperItems;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

@Deprecated(forRemoval = true)
public class CorruptionVisual extends ItemAbility {

    private final int radius;

    public CorruptionVisual(int radius) {
        super("corruption_visual", AbilityAction.RIGHT_CLICK_ALL, 0, 0);

        this.addLineToLore(ChatColor.GRAY + "lol if you have this item then please contact");
        this.addLineToLore(ChatColor.GRAY + "ZenoX#3202 on " + ChatColor.DARK_PURPLE + "discord");

        this.radius = radius;
    }

    @Override
    protected void runExecutable(Event event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();
        double rad = this.radius;

        // Particle Helix

        new BukkitRunnable() {


            final double height = rad;
            final double yIncrement = 0.02;
            final int wings = 6;
            final Random r = new Random();
            final Location loc = p.getLocation();
            final double maxRadius = rad;
            double a = 0;
            int count = 0;
            double x = 0;
            double y = 0;
            double z = 0;
            double radius = 0.8;

            @Override
            public void run() {
                for (int i = 0; i < wings; i++) {
                    x = Math.cos(a + i * (2 * Math.PI / wings)) * radius % maxRadius;
                    z = Math.sin(a + i * (2 * Math.PI / wings)) * radius % maxRadius;

                    Location loc2 = new Location(loc.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y % height + 1), (float) (loc.getZ() + z));
                    p.getWorld().spawnParticle(Particle.SOUL, loc2, 0);
                    p.getWorld().spawnParticle(Particle.REVERSE_PORTAL, loc2, 0);
                    p.getWorld().spawnParticle(Particle.SQUID_INK, loc2, 0);

                    a += Math.PI / 300;
                    count += 1;
                    radius += 0.01;
                    y += yIncrement;
                }
            }
        }.runTaskTimer(SuperItems.getPlugin(), 1, 1);
    }
}
