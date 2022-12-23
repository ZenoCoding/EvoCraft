package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Geo;
import me.zenox.superitems.util.TriConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class AttackAbility extends Ability<EntityDamageByEntityEvent> {


    public AttackAbility(AbilitySettings settings) {
        super(settings);
    }

    public AttackAbility(AbilitySettings settings, TriConsumer<EntityDamageByEntityEvent, Player, ItemStack> executable) {
        super(settings, executable);
    }

    public AttackAbility(String id, int manaCost, double cooldown, TriConsumer<EntityDamageByEntityEvent, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, Slot.MAIN_HAND, exectuable);
    }

    @Override
    protected boolean checkEvent(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player && !Util.isInvulnerable(event.getEntity());
    }

    @Override
    Player getPlayerOfEvent(EntityDamageByEntityEvent e) {
        return ((Player) e.getDamager());
    }

    @Override
    List<ItemStack> getItem(Player p, EntityDamageByEntityEvent e) {
        return this.getSlot().item(p);
    }

    // Static ability executables
    public static void justiceAbility(EntityDamageByEntityEvent event, Player p, ItemStack item) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0));
        p.playSound(p.getLocation(), Sound.ITEM_AXE_SCRAPE, 1, 1.4f);
        if (new Random().nextInt(3) == 0) event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
    }

    public static void darkFuryAbility(EntityDamageByEntityEvent event, Player p, ItemStack item) {
        List<MetadataValue> values = p.getMetadata("last_damaged");
        if (values.isEmpty() || (System.currentTimeMillis() - values.get(0).asLong() > 60 * 1000)) {
            event.setDamage(event.getDamage()*1.5);
        }

    }

    public static void vertexAbility(EntityDamageByEntityEvent event, Player p, ItemStack item) {

        World w = event.getEntity().getWorld();

        p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 0f);

        // get # of stacks
        int stacks = 1;
        List<MetadataValue> stackValues = p.getMetadata("vertex_stack");
        List<MetadataValue> idValues = p.getMetadata("vertex_hit_id");
        if (!stackValues.isEmpty() && !idValues.isEmpty() && idValues.get(0).asInt() == event.getEntity().getEntityId()) {
            stacks = stackValues.get(0).asInt();
            p.setMetadata("vertex_stack", new FixedMetadataValue(SuperItems.getPlugin(), stacks + 1));
        } else {
            p.setMetadata("vertex_stack", new FixedMetadataValue(SuperItems.getPlugin(), 1));
        }
        p.setMetadata("vertex_hit_id", new FixedMetadataValue(SuperItems.getPlugin(), event.getEntity().getEntityId()));

        Util.sendMessage(p, "Stacks: " + stacks);


        if (stacks >= 3) {
            event.setDamage(event.getDamage() * (1 + 0.04 * stacks));
        }


        List<MetadataValue> taskIdValues = p.getMetadata("vertex_task_id");
        if (taskIdValues.isEmpty() || !Bukkit.getScheduler().isQueued(taskIdValues.get(0).asInt())) {
            BukkitTask vertexDisplayTask = new BukkitRunnable() {
                int count = 1;
                int truecount = 0;
                int oldstacks = -1;

                @Override
                public void run() {


                    int stacks = 1;
                    List<MetadataValue> countValues = p.getMetadata("vertex_stack");
                    if (!countValues.isEmpty()) {
                        stacks = countValues.get(0).asInt();
                    }

                    if (truecount % (Math.round(15 / Math.sqrt(count)) + 5) == 0 && oldstacks == stacks) {
                        p.setMetadata("vertex_stack", new FixedMetadataValue(SuperItems.getPlugin(), stacks -= 1));
                    }

                    oldstacks = stacks;

                    if (stacks <= 0) {
                        cancel();
                        p.setMetadata("vertex_stack", new FixedMetadataValue(SuperItems.getPlugin(), 0));
                    }


                    if (stacks >= 3) {
                        // Create Dodecahedron
                        List<Vector> edgedDodecahedron = Geo.lerpEdges(Geo.makeDodecahedron(p.getLocation().toVector(), 2), 7);

                        for (Vector v : edgedDodecahedron) {
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(5, 165, 255), 0.6F);
                            w.spawnParticle(Particle.REDSTONE, v.rotateAroundAxis(p.getLocation().toVector(), Math.toRadians(((float) count) / 4f % 360)).toLocation(w).add(0, 1.5, 0), 1, dustOptions);
                        }
                    }

                    count += Math.sqrt(stacks) * 6;
                    truecount++;
                }
            }.runTaskTimer(SuperItems.getPlugin(), 0, 7);


            p.setMetadata("vertex_task_id", new FixedMetadataValue(SuperItems.getPlugin(), vertexDisplayTask.getTaskId()));
        }
    }


}
