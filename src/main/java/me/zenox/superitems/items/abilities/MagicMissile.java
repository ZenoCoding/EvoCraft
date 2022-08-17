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
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

import static me.zenox.superitems.util.Util.getNearbyBlocks;

public class MagicMissile extends ItemAbility {

    private final boolean combustion;
    private final int explosionpower;
    public MagicMissile(int explosionpower,boolean combustion) {
        super("Magic Missile", "magic_missile", AbilityAction.RIGHT_CLICK_ALL, 0, 0);
        this.explosionpower = explosionpower;
        this.combustion = combustion;

        this.addLineToLore(ChatColor.GRAY + "Shoots a magic missile that explodes");
        this.addLineToLore(ChatColor.GRAY + "on impact and deals massive" + ChatColor.RED + " damage.");

        if (combustion == true) {
            this.addLineToLore("");
            this.addLineToLore(ChatColor.GRAY + "20% chance for the item to " + ChatColor.GOLD + "combust " + ChatColor.GRAY + "and dissapear.");
        }
    }

    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();

        LocalPlayer localPlayer;
        com.sk89q.worldedit.util.Location guardLoc;
        RegionContainer container;
        RegionQuery query;

        if (SuperItems.getPlugin().isUsingWorldGuard) {
            localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            guardLoc = BukkitAdapter.adapt(p.getLocation());
            container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            query = container.createQuery();

            if (!query.testState(guardLoc, localPlayer, Flags.BUILD)) {
                Util.sendMessage(p, "You cannot use this item in a worldguard region! Flags: [BUILD]");
                return;
            }
        }

        // 20% chance to remove an item from their hand
        if (r.nextInt(5) == 0 && this.combustion == true) {
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            Util.sendMessage(p, ChatColor.GOLD + "Woah! Your " + ChatColor.ITALIC + "Magic Toy Stick " + ChatColor.GOLD + "combusted in your hand!", false);
            p.damage(5, p);
            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.5F);
        }


        Trident trident = (Trident) w.spawnEntity(p.getLocation().add(0, 1.8, 0), EntityType.TRIDENT);
        Vector v = p.getLocation().getDirection().normalize().clone();
        Vector v2 = v.multiply(3);
        trident.setVelocity(v2);
        trident.setDamage(0);
        trident.setGravity(false);
        trident.setPierceLevel(127);

        int explosionPower = this.explosionpower;

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                trident.setVelocity(v2);
                Location loc = trident.getLocation();
                for (Entity entity :
                        trident.getNearbyEntities(2, 2, 2)) {
                    if (entity instanceof Damageable && !entity.equals(p)) {
                        ((Damageable) entity).damage(explosionPower, p);
                    }
                }
                for (int i = 0; i < 5; i++) {
                    w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.ELECTRIC_SPARK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.END_ROD, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                }

                if (SuperItems.getPlugin().isUsingWorldGuard) {
                    LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                    com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(trident.getLocation());
                    RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query2 = container2.createQuery();

                    if (!query2.testState(guardLoc2, localPlayer2, Flags.BUILD)) {
                        trident.remove();
                        Util.sendMessage(p, "You cannot shoot this item into a worldguard region! Flags: [PVP]");
                        cancel();
                    }
                }

                if (trident.isInBlock()) {
                    trident.remove();
                    List<Block> blocks = getNearbyBlocks(loc, explosionPower / 2, explosionPower / 4);
                    for (Block block : blocks) {
                        if (block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))
                            continue;
                        if (r.nextDouble() > 0.7) continue;
                        if (SuperItems.getPlugin().isUsingWorldGuard) {
                            LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                            com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(block.getLocation());
                            RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionQuery query2 = container2.createQuery();


                            if (!query2.testState(guardLoc2, localPlayer2, Flags.BUILD)) {
                                continue;
                            }
                        }
                        FallingBlock fallingBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                        fallingBlock.setVelocity(fallingBlock.getLocation().toVector().subtract(loc.clone().add(r.nextDouble() * 4 - 2, -2, r.nextDouble() * 4 - 2).toVector()).multiply(explosionPower * 2).normalize());
                        fallingBlock.setDropItem(false);
                        fallingBlock.setHurtEntities(true);
                        fallingBlock.setMetadata("temporary", new FixedMetadataValue(SuperItems.getPlugin(), true));
                    }
                    try {
                        trident.remove();
                    } catch (Exception e) {

                    }
                    w.createExplosion(loc, explosionPower, false, false, p);
                    cancel();
                }
                count++;
                if (count >= 600) {
                    trident.remove();
                    cancel();
                }

            }
        }.runTaskTimer(SuperItems.getPlugin(), 0, 2);
    }
}
