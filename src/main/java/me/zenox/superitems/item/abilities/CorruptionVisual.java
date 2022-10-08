package me.zenox.superitems.abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Geo;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

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

            double a = 0;
            int count = 0;

            double x = 0;
            double y = 0;
            double z = 0;

            final int wings = 6;

            final Random r = new Random();

            final Location loc = p.getLocation();

            double radius = 0.8;

            final double maxRadius = rad;

            @Override
            public void run() {
                for (int i = 0; i < wings; i++) {
                    x = Math.cos(a + i * (2 * Math.PI / wings)) * radius%maxRadius;
                    z = Math.sin(a + i * (2 * Math.PI / wings)) * radius%maxRadius;

                    Location loc2 = new Location(loc.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y%height + 1), (float) (loc.getZ() + z));
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
