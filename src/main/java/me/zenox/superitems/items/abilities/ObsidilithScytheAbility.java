package me.zenox.superitems.items.abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ModeledEntity;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ObsidilithScytheAbility extends ItemAbility {

    private final static int SHARD_RADIUS = 3;
    private final static int SHARD_SPEED = 3;

    public ObsidilithScytheAbility() {
        super("Obsidi-strike", "obsidian_projectile", AbilityAction.RIGHT_CLICK_ALL, 75, 0);

        this.addLineToLore(ChatColor.GRAY + "Shoots a sharpened shard of " + ChatColor.DARK_GRAY + "obsidian" + ChatColor.GRAY + " that");
        this.addLineToLore(ChatColor.GRAY + "deals massive" + ChatColor.RED + " damage" + ChatColor.GRAY + " on impact.");
    }

    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        World w = p.getWorld();

        for (float i = 0; i < 3; i++) {
            i+=.5;
            shootShard(p, new Vector(Math.cos((i)*(Math.PI/4))*SHARD_RADIUS, Math.sin((i)*(Math.PI/3))*SHARD_RADIUS, 0d).rotateAroundY(p.getLocation().getYaw()%180).toLocation(p.getWorld()), (i+1)*0.2);
            i-=.5;
        }

        shootShard(p, new Location(p.getWorld(), 0, 1.8, 0), 0);

    }

    // Refactor into smaller helper methods
    private void shootShard(Player p, Location deltaLoc, double delay){
        Random r = new Random();
        World w = p.getWorld();

        Arrow arrow = (Arrow) w.spawnEntity(p.getLocation().add(deltaLoc), EntityType.ARROW);
        Vector v = p.getLocation().getDirection().normalize().clone();
        Vector v2 = v.multiply(SHARD_SPEED);
        arrow.setShooter(p);
        arrow.setGravity(false);
        arrow.setPierceLevel(123);
        arrow.getLocation().setDirection(v);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arrow.setInvulnerable(true);
        arrow.setKnockbackStrength(0);
        arrow.setCritical(false);
        arrow.setDamage(0d);

        ModeledEntity shard = ModelEngineAPI.createModeledEntity(arrow);
        shard.addModel(ModelEngineAPI.createActiveModel("obsidianshard"), true);
        shard.getLookController().setBodyYaw(p.getLocation().getYaw());
//        Util.sendMessage(p, "Yaw: " + p.getLocation().getYaw());
//        Util.sendMessage(p, "Pitch: " + p.getLocation().getPitch());
        //shard.getLookController().setPitch(p.getLocation().getPitch());
        shard.getModel("obsidianshard").setLockPitch(true);
        shard.getModel("obsidianshard").setLockYaw(true);

        w.playSound(p.getLocation(), Sound.ENTITY_BEE_HURT, 1, 0.2f);

        new BukkitRunnable() {
            double count = 0;

            @Override
            public void run() {

                Location loc = arrow.getLocation();
                for (Entity entity :
                        arrow.getNearbyEntities(0.5, 2, 0.5)) {
                    if (SuperItems.getPlugin().isUsingWorldGuard) {
                        LocalPlayer localPlayer2 = WorldGuardPlugin.inst().wrapPlayer(p);
                        com.sk89q.worldedit.util.Location guardLoc2 = BukkitAdapter.adapt(arrow.getLocation());
                        RegionContainer container2 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                        RegionQuery query2 = container2.createQuery();
                        if (!query2.testState(guardLoc2, localPlayer2, Flags.BUILD)) {
                            break;
                        }

                    }
                    if (entity instanceof Damageable && !entity.equals(p)) {
                        ((Damageable) entity).damage(20, p);
                        if(entity instanceof LivingEntity) ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 1));
                    }
                }

                if(count/20 == delay) {
                    w.playSound(loc, Sound.ITEM_AXE_WAX_OFF, 1, 1.2f);
                } else if(count/20 > delay){
                    arrow.setVelocity(v2);
                    if(count%20 == 0){
                        w.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 0.7f, 0.4f);
                    }
                }

                if(count/20 > 30) {
                    cancel();
                    arrow.remove();
                }

                if(arrow.isInBlock()){
                    arrow.remove();
                    cancel();
                } else {
                    for (int i = 0; i < 2; i++) {
                        w.spawnParticle(Particle.SQUID_INK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                        w.spawnParticle(Particle.REVERSE_PORTAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                        w.spawnParticle(Particle.BLOCK_CRACK, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2, Material.OBSIDIAN.createBlockData());
                        w.spawnParticle(Particle.SMOKE_NORMAL, loc, 0, r.nextDouble() - 0.5, r.nextDouble() - 0.5, r.nextDouble() - 0.5, 0.2);
                    }
                }

                count = count + 1;

            }
        }.runTaskTimer(SuperItems.getPlugin(), 0, 1);
    }
}
