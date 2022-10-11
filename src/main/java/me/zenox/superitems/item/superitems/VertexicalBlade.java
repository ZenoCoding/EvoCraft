package me.zenox.superitems.item.superitems;

import com.archyx.aureliumskills.stats.Stats;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.util.Geo;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VertexicalBlade extends ComplexItem implements Listener {
    public VertexicalBlade() {
        super("vertex_blade", true, true, Rarity.RARE, Type.MISC, Material.AMETHYST_SHARD, Map.of(Stats.STRENGTH, 100d, Stats.WISDOM, 300d), List.of());

        List<String> lore = List.of(ChatColor.GRAY + "Using " + ChatColor.LIGHT_PURPLE + "vertexical math and convex geometry,",
                ChatColor.GRAY + "this blade bends the fabric of " + ChatColor.DARK_GRAY + "space-time.",
                "",
                ChatColor.GOLD + "Item Ability: Atom-split Alignment",
                ChatColor.GRAY + "For every " + ChatColor.RED + "consecutive" + ChatColor.GRAY + " hit on an entity,",
                ChatColor.GRAY + "construct a point on a " + ChatColor.GOLD + "Tesseract," + ChatColor.GRAY + " increasing ",
                ChatColor.RED + "damage" + ChatColor.GRAY + " against that enemy.");

        this.getMeta().setLore(lore);
        this.getMeta().isUnbreakable();

        Multimap<Attribute, AttributeModifier> attributeMap = ArrayListMultimap.create();
        attributeMap.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "superitems:attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        attributeMap.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "superitems:attack_speed", -2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        this.getMeta().setAttributeModifiers(attributeMap);

        this.getMeta().addEnchant(Enchantment.DAMAGE_ALL, 7, true);
        this.getMeta().addEnchant(Enchantment.LOOT_BONUS_MOBS, 5, true);

        Bukkit.getPluginManager().registerEvents(this, SuperItems.getPlugin());
    }

    // TODO: Refactor this to AttackAbility

    @EventHandler
    public void onDamageEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            World w = p.getWorld();
            ItemStack item = p.getInventory().getItemInMainHand();
            ComplexItem complexItem = ItemRegistry.byItem(item);
            if (!(complexItem == null) && complexItem.getId() == this.getId() && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 0f);

                // get # of stacks
                Integer stacks = 1;
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


                            Integer stacks = 1;
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
                                    w.spawnParticle(Particle.REDSTONE, v.rotateAroundAxis(p.getLocation().toVector(), Math.toRadians(count / 4 % 360)).toLocation(w).add(0, 1.5, 0), 1, dustOptions);
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
    }
}
