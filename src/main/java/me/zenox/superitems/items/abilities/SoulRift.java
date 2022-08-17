package me.zenox.superitems.items.abilities;

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
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.zenox.superitems.util.Util.getNearbyBlocks;

public class SoulRift extends ItemAbility {
    public SoulRift() {
        super("Soul Rift", "soul_rift", AbilityAction.RIGHT_CLICK_ALL, 50, 60);

        this.addLineToLore(ChatColor.GRAY + "Deploys a" + ChatColor.AQUA + " soul crystal" + ChatColor.GRAY + ", which lasts for" + ChatColor.GREEN + " 5s" + ChatColor.GRAY + " and");
        this.addLineToLore(ChatColor.GRAY + "pulls entities in, dealing " + ChatColor.RED + "true damage" + ChatColor.GRAY + " every second.");
    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Action action = e.getAction();
        Player p = e.getPlayer();
        World w = p.getWorld();
        Location loc;
        boolean allowed = true;
        if (SuperItems.getPlugin().isUsingWorldGuard) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            com.sk89q.worldedit.util.Location guardLoc = BukkitAdapter.adapt(p.getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            allowed = query.testState(guardLoc, localPlayer, Flags.BUILD);
        }
        if (!allowed) {
            Util.sendMessage(p, "You cannot use this item in a worldguard region!");
            return;
        }

        e.setCancelled(true);

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            loc = e.getClickedBlock().getLocation();
        } else if (action.equals(Action.RIGHT_CLICK_AIR)) {
            loc = p.getLocation();
        } else {
            return;
        }

        e.setCancelled(true);

        EnderCrystal crystal = (EnderCrystal) w.spawnEntity(loc.add(0, 2, 0), EntityType.ENDER_CRYSTAL);
        crystal.setInvulnerable(true);
        crystal.setMetadata("dmgEnv", new FixedMetadataValue(SuperItems.getPlugin(), false));

        new BukkitRunnable() {
            final List<Block> blocks = getNearbyBlocks(crystal.getLocation(), 7, 2);
            final List<FallingBlock> fBlocks = new ArrayList<>();
            final List<LivingEntity> entities = new ArrayList<>();
            int count = 0;

            @Override
            public void run() {
                // Particle Magic
                List<Vector> dodecahedron = Geo.MakeDodecahedron(loc.toVector(), 2);
                for(Vector v : dodecahedron){
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 187, 215), 0.5F);
                    w.spawnParticle(Particle.REDSTONE, v.toLocation(w).add(0, 0.5+Math.sin(count)/4, 0), 1, dustOptions);
                }


                // At 5 seconds

                if (count == 100) {
                    cancel();
                    crystal.remove();
                    w.createExplosion(crystal.getLocation(), 3, false, false);
                    for (FallingBlock fallingblock : fBlocks) {
                        fallingblock.setGravity(true);
                    }
                    for (LivingEntity entity : entities) {
                        entity.setGravity(true);
                    }

                }

                Random r = new Random();
                Block block = blocks.get(r.nextInt(blocks.size() - 0) + 0);

                if (!(block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))) {
                    FallingBlock fBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                    fBlock.setVelocity((fBlock.getLocation().toVector().subtract(crystal.getLocation().toVector()).multiply(-10).normalize()));
                    fBlock.setGravity(false);
                    fBlock.setDropItem(false);
                    fBlock.setHurtEntities(true);
                    fBlock.setMetadata("temporary", new FixedMetadataValue(SuperItems.getPlugin(), true));

                    fBlocks.add(fBlock);
                }

                for (FallingBlock fallingBlock : fBlocks) {
                    if (r.nextInt(count / 25 + 1) == 0) {
                        fallingBlock.setVelocity((fallingBlock.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble() - 0.5, r.nextDouble() - 0.5 + 2, r.nextDouble() - 0.5).toVector()).multiply(-0.5).normalize()));
                    }
                }

                for (LivingEntity entity : entities) {
                    if (r.nextInt(count / 25 + 1) == 0 && !entity.equals(p)) {
                        entity.setVelocity((entity.getLocation().toVector().subtract(crystal.getLocation().add(r.nextDouble() - 0.5, r.nextDouble() - 0.5 + 2, r.nextDouble() - 0.5).toVector()).multiply(-0.5).normalize()));
                        entity.damage((entity.getHealth() * (2 / 3)) / 10 + 1);
                    }
                }

                if (count == 0) {
                    for (Entity entity : crystal.getNearbyEntities(7.5, 5, 7.5)) {
                        if (entity instanceof LivingEntity && !entity.equals(p)) {
                            entity.setVelocity((entity.getLocation().toVector().subtract(crystal.getLocation().add(0, 2, 0).toVector()).multiply(-10).normalize()));
                            entity.setGravity(false);
                            entities.add((LivingEntity) entity);
                        }
                    }
                }
                count++;
            }
        }.runTaskTimer(SuperItems.getPlugin(), 0, 0);
    }
}
