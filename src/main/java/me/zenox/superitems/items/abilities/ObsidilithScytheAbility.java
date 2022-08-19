package me.zenox.superitems.items.abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ObsidilithScytheAbility extends ItemAbility {


    
    public ObsidilithScytheAbility() {
        super("Obsid-strike", "obsidian_projectile", AbilityAction.RIGHT_CLICK_ALL, 2, 0);

        this.addLineToLore(ChatColor.GRAY + "Shoots a sharpened shard of" + ChatColor.BLACK + "obsidian.");
        this.addLineToLore(ChatColor.GRAY + "Deals massive" + ChatColor.RED + " damage" + ChatColor.GRAY + " on impact.");


    }

    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();

        DragonFireball fireball = (DragonFireball) w.spawnEntity(p.getLocation().add(0, 1.8, 0), EntityType.DRAGON_FIREBALL);
        Vector v = p.getLocation().getDirection().normalize().clone();
        Vector v2 = v.multiply(10);
        fireball.setVelocity(v2);
        fireball.setShooter(p);
        fireball.setGravity(false);
        fireball.setBounce(true);
        fireball.setIsIncendiary(true);
        fireball.setGlowing(true);

        SpectralArrow arrow = (SpectralArrow) w.spawnEntity(p.getLocation().add(0, 1.8, 0), EntityType.SPECTRAL_ARROW);
        Vector v = p.getLocation().getDirection().normalize().clone();
        Vector v2 = v.multiply(5);
        arrow.setVelocity(v2);
        arrow.setShooter(p);
        arrow.setGravity(false);
        arrow.setBounce(true);
        arrow.setDamage(100);
        fireball.setGlowing(true);

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                fireball.setVelocity(v2);
                Location loc = fireball.getLocation();
                for (Entity entity :
                        fireball.getNearbyEntities(2, 2, 2)) {
                    if (entity instanceof Damageable && !entity.equals(p)) {
                        ((Damageable) entity).damage(10, p);
                    }
                }

                arrow.setVelocity(v2);
                Location loc1 = arrow.getLocation();
                for (Entity entity :
                        arrow.getNearbyEntities(2, 2, 2)) {
                    if (entity instanceof Damageable && !entity.equals(p)) {
                        ((Damageable) entity).damage(100, p);
                    }
                }
                for (int i = 0; i < 5; i++) {
                    w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.SQUID_INK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                    w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                }

                if (SuperItems.getPlugin().isUsingWorldGuard) {
                    LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                    com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(fireball.getLocation());
                    RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query2 = container2.createQuery();


                }

            }
        }.runTaskTimer(SuperItems.getPlugin(), 0, 1);
    }
}
