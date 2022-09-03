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
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.zenox.superitems.util.Util.getNearbyBlocks;

public class Corruption extends ItemAbility {

    private final int radius;
    private final boolean blackhole;
    private final double duration;

    public Corruption(int radius, boolean blackhole, double duration) {
        super("Corruption", "corruption", AbilityAction.RIGHT_CLICK_ALL, 0, 0);
        this.blackhole = blackhole;
        this.duration = duration;

        this.addLineToLore(ChatColor.GRAY + "lol if you have this item then please contact");
        this.addLineToLore(ChatColor.GRAY + "ZenoX#3202 on " + ChatColor.DARK_PURPLE + "discord");

        this.radius = radius;
    }

    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();
        double rad = this.radius;
        double duration = this.duration;

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


            final List<FallingBlock> fBlocks = new ArrayList<>();

            @Override
            public void run() {

                final List<Block> blocks = getNearbyBlocks(loc, 7, 2);
                Random r = new Random();
                Block block = blocks.get(r.nextInt(blocks.size() - 0) + 0);

                if(blackhole) {
                    if (!(block.getType().getBlastResistance() > 1200 || block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD))) {
                        FallingBlock fBlock = w.spawnFallingBlock(block.getLocation(), block.getBlockData());
                        fBlock.setVelocity((fBlock.getLocation().toVector().subtract(loc.toVector()).multiply(-10).normalize()));
                        fBlock.setGravity(false);
                        fBlock.setDropItem(false);
                        fBlock.setHurtEntities(true);
                        fBlock.setMetadata("temporary", new FixedMetadataValue(SuperItems.getPlugin(), true));

                        fBlocks.add(fBlock);
                    }

                    if(fBlocks.size() > 25){
                        fBlocks.get(0).setGravity(true);
                        fBlocks.remove(0);
                    }

                    for (FallingBlock fallingBlock : fBlocks) {
                        if (r.nextInt(count / 25 + 1) == 0) {
                            fallingBlock.setVelocity((fallingBlock.getLocation().toVector().subtract(loc.clone().add(r.nextDouble() - 0.5, r.nextDouble() - 0.5 + 2, r.nextDouble() - 0.5).toVector()).multiply(-0.5).normalize()));
                        }
                    }
                }
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

                    count++;


                    if (count/20 >= duration) {
                        displayPentagon(p, loc, rad);
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(SuperItems.getPlugin(), 1, 1);
    }

    private void displayPentagon(Player p, Location loc, double radius){
        loc.add(0, radius*3/2, 0);
        List<Vector> points = Geo.makeDodecahedron(loc.toVector(), radius*3/2);
        World w = loc.getWorld();
        new BukkitRunnable(){
            double count = 0;
            @Override
            public void run() {
                for(Vector v : points){
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(230, 103, 215), 2.5F);
                    w.spawnParticle(Particle.REDSTONE, v.rotateAroundAxis(loc.toVector(), count%360).toLocation(loc.getWorld()), 1, dustOptions);
                }

                if(count/20 > 5){
                    cancel();
                    for(Vector v : points){
                        w.createExplosion(v.toLocation(loc.getWorld()), 10, false, true);
                    }
                }

                count+=2;
            }
        }.runTaskTimer(SuperItems.getPlugin(), 0, 1);
    }
}
