package me.zenox.superitems.abilities;

import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.util.Geo;
import me.zenox.superitems.util.TriConsumer;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

public class AttackAbility extends Ability {

    public AttackAbility(String id, int manaCost, double cooldown) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.MAIN_HAND);
    }

    public AttackAbility(String id, int manaCost, double cooldown, Slot slot) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, slot);
    }

    public AttackAbility(String id, int manaCost, double cooldown, TriConsumer<Event, Player, ItemStack> exectuable) {
        super(id, manaCost, cooldown, EntityDamageByEntityEvent.class, Slot.MAIN_HAND, exectuable);
    }

    @Override
    protected boolean checkEvent(Event e) {
        return e instanceof EntityDamageByEntityEvent event && event.getDamager() instanceof Player && !Util.isInvulnerable(event.getEntity());
    }

    @Override
    Player getPlayerOfEvent(Event e) {
        return ((Player) ((EntityDamageByEntityEvent) e).getDamager());
    }

    @Override
    List<ItemStack> getItem(Player p, Event e) {
        return this.getSlot().item(p);
    }

    // Static ability executables
    public static void justiceAbility(Event event, Player p, ItemStack item) {
        EntityDamageByEntityEvent e = ((EntityDamageByEntityEvent) event);
        ComplexItem complexItem = ItemRegistry.byItem(item);
        //if (!(complexItem == null) && complexItem.getId().equalsIgnoreCase("sword_of_justice") && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0));
        p.playSound(p.getLocation(), Sound.ITEM_AXE_SCRAPE, 1, 1.4f);
        if (new Random().nextInt(3) == 0) e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
        //}
    }

    public static void darkFuryAbility(Event event, Player p, ItemStack item) {
        EntityDamageByEntityEvent e = ((EntityDamageByEntityEvent) event);
        List<MetadataValue> values = p.getMetadata("last_damaged");
        if (values.isEmpty() || (System.currentTimeMillis() - values.get(0).asLong() > 30 * 1000)) {
            e.setDamage(e.getDamage()*2);
        }

    }

    public static void vertexAbility(Event event, Player p, ItemStack item) {
        EntityDamageByEntityEvent e = ((EntityDamageByEntityEvent) event);

        World w = e.getEntity().getWorld();

        p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 0f);

        // get # of stacks
        int stacks = 1;
        List<MetadataValue> stackValues = p.getMetadata("vertex_stack");
        List<MetadataValue> idValues = p.getMetadata("vertex_hit_id");
        if (!stackValues.isEmpty() && !idValues.isEmpty() && idValues.get(0).asInt() == e.getEntity().getEntityId()) {
            stacks = stackValues.get(0).asInt();
            p.setMetadata("vertex_stack", new FixedMetadataValue(SuperItems.getPlugin(), stacks + 1));
        } else {
            p.setMetadata("vertex_stack", new FixedMetadataValue(SuperItems.getPlugin(), 1));
        }
        p.setMetadata("vertex_hit_id", new FixedMetadataValue(SuperItems.getPlugin(), e.getEntity().getEntityId()));

        Util.sendMessage(p, "Stacks: " + stacks);


        if (stacks >= 3) {
            e.setDamage(e.getDamage() * (1 + 0.04 * stacks));
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
