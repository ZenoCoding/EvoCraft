package me.zenox.superitems.enchant;

import me.zenox.superitems.Slot;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantRegistry {

    // Custom Enchantments

    public static ComplexEnchantment SHARPNESS = new AttackEnchantment("sharpness", 5, 30, List.of(ComplexItem.Type.SWORD, ComplexItem.Type.AXE), Slot.MAIN_HAND, new ArrayList<>(), EnchantRegistry::sharpnessExecutable);

    public static ComplexEnchantment FIRE_ASPECT = new AttackEnchantment("fire_aspect", 2, 20, List.of(ComplexItem.Type.SWORD, ComplexItem.Type.AXE), Slot.MAIN_HAND, new ArrayList<>(), EnchantRegistry::fireAspectExecutable);

    public static ComplexEnchantment SIPHON = new AttackEnchantment("siphon", 5, 10, List.of(ComplexItem.Type.SWORD, ComplexItem.Type.AXE), Slot.MAIN_HAND, new ArrayList<>(), EnchantRegistry::syphonExecutable);

    public static ComplexEnchantment DARKSOUL = new AttackEnchantment("darksoul", 5, 0, List.of(ComplexItem.Type.SWORD, ComplexItem.Type.AXE), Slot.MAIN_HAND, new ArrayList<>(), EnchantRegistry::darksoulExecutable);

    public static ComplexEnchantment KNOCKBACK = new AttackEnchantment("knockback", 2, 20, List.of(ComplexItem.Type.SWORD), Slot.MAIN_HAND, new ArrayList<>(), EnchantRegistry::knockbackExecutable);

    public static ComplexEnchantment CULLING = new AttackEnchantment("culling", 2, 20, List.of(ComplexItem.Type.SWORD), Slot.MAIN_HAND, new ArrayList<>(), EnchantRegistry::cullingExecutable);

    public static ComplexEnchantment PROTECTION = new DamagedByEntityEnchantment("protection", 4, 20, List.of(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS), Slot.ARMOR, new ArrayList<>(), EnchantRegistry::protectionExecutable);

    // Vanilla Enchantments
    public static ComplexEnchantment AQUA_AFFINITY = new DamagedByEntityEnchantment("aqua_affinity", 4, 20, List.of(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS), Slot.ARMOR, new ArrayList<>(), EnchantRegistry::protectionExecutable);


    // static function methods
    private static void sharpnessExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        event.setDamage(event.getDamage() * (1 + 0.1f * level));
    }

    private static void fireAspectExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        if (event.getEntity() instanceof LivingEntity && !event.getEntity().isInvulnerable()) {
            LivingEntity entity = ((LivingEntity) event.getEntity());
            entity.setFireTicks(20 * level);
            new BukkitRunnable() {
                int a = 0;
                @Override
                public void run() {
                    entity.damage(level*2);
                    if (a * 10 >= 20 * level) this.cancel();
                    a++;
                }
            }.runTaskTimer(SuperItems.getPlugin(), 0, 10);
        }
    }

    private static void syphonExecutable(Event e, Integer level, ItemStack item, Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, level));
    }

    private static void darksoulExecutable(Event event, Integer level, ItemStack item, Player p) {
        Random r = new Random();
        // 3-15% chance to...
        if (r.nextDouble() <= level * 0.05) {
            List<Monster> entities = new ArrayList<>();
            entities.addAll(((EntityDamageByEntityEvent) event).getEntity().getNearbyEntities(10, 10, 10).stream()
                    .filter(entity -> entity instanceof LivingEntity && entity instanceof Monster)
                    .map(entity -> ((Monster) entity))
                    .toList());

            if(entities.isEmpty()) return;

            Monster randEntity = entities.get(r.nextInt(entities.size()));
            shootSkull(p, ((EntityDamageByEntityEvent) event).getEntity().getLocation(), randEntity.getLocation(), 3 + level);
            entities.remove(randEntity);

            if(entities.isEmpty()) return;

            if(level >= 5) shootSkull(p, ((EntityDamageByEntityEvent) event).getEntity().getLocation(), entities.get(r.nextInt(entities.size())).getLocation(), 3 + level);

        }
    }

    private static void shootSkull(Player p, Location initial, Location target, int yield){
        Location eyeLoc = p.getEyeLocation();
        WitherSkull f = (WitherSkull) eyeLoc.getWorld().spawnEntity(eyeLoc.add(eyeLoc.getDirection()), EntityType.WITHER_SKULL);
        f.setVelocity(target.toVector().subtract(initial.toVector()).normalize().multiply(1.5));
        f.setMetadata("dmgEnv", new FixedMetadataValue(SuperItems.getPlugin(), false));
        f.setShooter(p);
        f.setYield(yield);
    }

    private static void knockbackExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        Entity entity = event.getEntity();
        entity.setVelocity(entity.getVelocity().add(event.getDamager().getLocation().getDirection().normalize().multiply(level/2)));
    }

    private static void cullingExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        Entity entity = event.getEntity();
        entity.setVelocity(entity.getVelocity().add(event.getDamager().getLocation().getDirection().normalize().multiply(-1/2f*level)));
    }

    private static void protectionExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        event.setDamage(event.getDamage()*(1-level/20));
    }

    public static void registerEnchants() {
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + ComplexEnchantment.getRegisteredEnchants().size() + ChatColor.WHITE + " enchantments.");
    }

}
