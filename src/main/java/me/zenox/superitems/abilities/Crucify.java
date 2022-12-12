package me.zenox.superitems.abilities;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ItemRegistry;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

import static me.zenox.superitems.item.ItemRegistry.CRUCIFIED_AMULET;

public class Crucify extends ItemAbility implements Listener {

    public Crucify() {
        super("crucify", AbilityAction.RIGHT_CLICK_ALL, 0, 10);

        this.addLineToLore(ChatColor.GRAY + "Disables all " + ChatColor.RED + "regeneration" + ChatColor.GRAY + " but gives");
        this.addLineToLore(ChatColor.GRAY + "a permanent " + ChatColor.RED + "strength and " + ChatColor.WHITE + " speed " + ChatColor.GRAY + "boost");
        this.addLineToLore("");
        this.addLineToLore(ChatColor.GRAY + "On death, this item and it's effects will disappear and");
        this.addLineToLore(ChatColor.GRAY + "create a large explosion, destroying everything in it's path.");

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    @Override
    public void runExecutable(PlayerInteractEvent event, Player p, ItemStack item) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2f, 0.7f);

        // Particle Helix

        new BukkitRunnable() {
            final String upDown = "up";

            final int height = 2;
            final double yIncrement = 0.02;
            final int wings = 6;
            final Random r = new Random();
            final Location loc = p.getLocation();
            final double radius = 0.8;
            double a = 0;
            int count = 0;
            double x = 0;
            double y = 0;
            double z = 0;

            @Override
            public void run() {
                for (int i = 0; i < wings; i++) {
                    x = Math.cos(a + i * (2 * Math.PI / wings)) * radius;
                    y = a / Math.PI;
                    z = Math.sin(a + i * (2 * Math.PI / wings)) * radius;

                    Location loc2 = new Location(p.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y + 1), (float) (loc.getZ() + z));
                    p.getWorld().spawnParticle(Particle.SOUL, loc2, 0);

                    a += Math.PI / 300;
                    count += 1;

                    if (count >= 360) cancel();
                }
            }
        }.runTaskTimer(SuperItems.getPlugin(), 3, 1);

        p.setMetadata("crucify_active", new FixedMetadataValue(SuperItems.getPlugin(), true));
    }


    @EventHandler
    public void onPlayerRegen(EntityRegainHealthEvent e) {
        Entity entity = e.getEntity();

        List<MetadataValue> values = entity.getMetadata("crucify_active");
        if (!values.isEmpty() && values.get(0).asBoolean()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();

        List<MetadataValue> values = p.getMetadata("crucify_active");
        if (!values.isEmpty() && values.get(0).asBoolean()) {
            p.setMetadata("crucify_active", new FixedMetadataValue(SuperItems.getPlugin(), false));
            Inventory inv = p.getInventory();
            for (ItemStack item : inv.getContents()) {
                ComplexItem complexItemMaterial = ItemRegistry.byItem(item);
                if (complexItemMaterial != null && complexItemMaterial.getId() == CRUCIFIED_AMULET.getId()) {
                    item.setAmount(item.getAmount() - 1);

                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 1f, 0);

                    // Particle Spiral Effect

                    new BukkitRunnable() {
                        final String upDown = "up";

                        final int height = 2;
                        final double yIncrement = 0.02;
                        final int wings = 6;
                        final Random r = new Random();
                        final Location loc = p.getLocation();
                        double a = 0;
                        int count = 0;
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        double radius = 0.8;

                        @Override
                        public void run() {
                            for (int i = 0; i < wings; i++) {
                                x = Math.cos(a + i * (2 * Math.PI / wings)) * radius;
                                y = Math.sin(a);
                                z = Math.sin(a + i * (2 * Math.PI / wings)) * radius;

                                Location loc2 = new Location(p.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y + 1), (float) (loc.getZ() + z));
                                p.getWorld().spawnParticle(Particle.SCULK_SOUL, loc2, 0);

                                for (Entity entity : p.getWorld().getNearbyEntities(loc2, 0.5, 0.5, 0.5)) {
                                    if (entity == p) continue;
                                    if (entity instanceof Damageable) {
                                        ((Damageable) entity).damage(19, e.getEntity());
                                        if (r.nextDouble() <= 0.1)
                                            p.getWorld().createExplosion(loc2, 1, false, false, p);
                                    }
                                }

                                a += Math.PI / 300;
                                count += 1;
                                radius += 0.015;

                                if (count >= 360) cancel();
                            }
                        }
                    }.runTaskTimer(SuperItems.getPlugin(), 3, 1);

                }
            }
        }
    }
}
