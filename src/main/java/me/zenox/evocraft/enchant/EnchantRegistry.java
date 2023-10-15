package me.zenox.evocraft.enchant;

import me.zenox.evocraft.Slot;
import me.zenox.evocraft.EvoCraft;
import me.zenox.evocraft.attribute.AttributeModifier;
import me.zenox.evocraft.attribute.AttributeRegistry;
import me.zenox.evocraft.item.ComplexItem;
import me.zenox.evocraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantRegistry {

    // Custom Enchantments

    public static ComplexEnchantment SIPHON = new AttackEnchantment(new EnchantmentSettings()
            .id("siphon")
            .maxLevel(5)
            .rarity(10)
            .types(ComplexItem.Type.SWORD, ComplexItem.Type.AXE)
            .slots(Slot.MAIN_HAND)
            .executable(EnchantRegistry::syphonExecutable));

    public static ComplexEnchantment DARKSOUL = new AttackEnchantment(new EnchantmentSettings()
            .id("darksoul")
            .maxLevel(5)
            .rarity(2)
            .types(ComplexItem.Type.SWORD, ComplexItem.Type.AXE)
            .slots(Slot.MAIN_HAND)
            .executable(EnchantRegistry::darksoulExecutable));

    public static ComplexEnchantment CULLING = new AttackEnchantment(new EnchantmentSettings()
            .id("culling")
            .maxLevel(2)
            .rarity(20)
            .types(ComplexItem.Type.SWORD)
            .slots(Slot.MAIN_HAND)
            .executable(EnchantRegistry::cullingExecutable)
            .exclusive(EnchantmentWrapper.KNOCKBACK));

    public static ComplexEnchantment SNIPE = new AttackEnchantment(new EnchantmentSettings()
            .id("snipe")
            .maxLevel(3)
            .rarity(10)
            .type(ComplexItem.Type.BOW)
            .slot(Slot.MAIN_HAND)
            .executable(EnchantRegistry::snipeExecutable));

    public static ComplexEnchantment VITALITY = new BasicEnchantment(new EnchantmentSettings()
            .id("vitality")
            .maxLevel(5)
            .rarity(0)
            .stats(new AttributeModifier("enchant.vitality", AttributeRegistry.HEALTH, 2, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, Slot.ARMOR))
            .types(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS)
            .slots(Slot.ARMOR));

    // Vanilla Enchantments
    // maybe make a mine enchant?

    public static ComplexEnchantment AQUA_AFFINITY = new BasicEnchantment(new EnchantmentSettings()
            .id("aqua_affinity")
            .maxLevel(1)
            .rarity(30)
            .types(ComplexItem.Type.HELMET)
            .slots(Slot.HEAD)
            .vanillaEnchant(Enchantment.WATER_WORKER));

    public static ComplexEnchantment BANE_OF_ARTHROPODS = new BasicEnchantment(new EnchantmentSettings()
            .id("bane_of_arthropods")
            .maxLevel(7)
            .rarity(30)
            .types(ComplexItem.Type.SWORD, ComplexItem.Type.AXE)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.DAMAGE_ARTHROPODS)
            .exclusive(EnchantmentWrapper.SHARPNESS, EnchantmentWrapper.SMITE));

    public static ComplexEnchantment BLAST_PROTECTION = new BasicEnchantment(new EnchantmentSettings()
            .id("blast_protection")
            .maxLevel(5)
            .rarity(30)
            .types(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS)
            .slots(Slot.ARMOR)
            .vanillaEnchant(Enchantment.PROTECTION_EXPLOSIONS)
            .exclusive(EnchantmentWrapper.PROTECTION, EnchantmentWrapper.FIRE_PROTECTION, EnchantmentWrapper.PROJECTILE_PROTECTION));

    public static ComplexEnchantment CHANNELING = new BasicEnchantment(new EnchantmentSettings()
            .id("channeling")
            .maxLevel(1)
            .rarity(10)
            .types(ComplexItem.Type.TRIDENT)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.CHANNELING)
            .exclusive(EnchantmentWrapper.RIPTIDE));

    public static ComplexEnchantment DEPTH_STRIDER = new BasicEnchantment(new EnchantmentSettings()
            .id("depth_strider")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.BOOTS)
            .slots(Slot.FEET)
            .vanillaEnchant(Enchantment.DEPTH_STRIDER)
            .exclusive(EnchantmentWrapper.FROST_WALKER));

    public static ComplexEnchantment EFFICIENCY = new BasicEnchantment(new EnchantmentSettings()
            .id("efficiency")
            .maxLevel(5)
            .rarity(40)
            .types(ComplexItem.Type.PICKAXE, ComplexItem.Type.AXE, ComplexItem.Type.SHOVEL)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.DIG_SPEED));

    public static ComplexEnchantment FEATHER_FALLING = new BasicEnchantment(new EnchantmentSettings()
            .id("feather_falling")
            .maxLevel(4)
            .rarity(10)
            .types(ComplexItem.Type.BOOTS)
            .slots(Slot.FEET)
            .vanillaEnchant(Enchantment.PROTECTION_FALL));

    public static ComplexEnchantment FIRE_ASPECT = new BasicEnchantment(new EnchantmentSettings()
            .id("fire_aspect")
            .maxLevel(2)
            .rarity(10)
            .types(ComplexItem.Type.SWORD)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.FIRE_ASPECT));

    public static ComplexEnchantment FIRE_PROTECTION = new BasicEnchantment(new EnchantmentSettings()
            .id("fire_protection")
            .maxLevel(5)
            .rarity(20)
            .types(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS)
            .slots(Slot.ARMOR)
            .vanillaEnchant(Enchantment.PROTECTION_FIRE)
            .exclusive(EnchantmentWrapper.BLAST_PROTECTION, EnchantmentWrapper.PROJECTILE_PROTECTION, EnchantmentWrapper.PROTECTION));

    public static ComplexEnchantment FLAME = new BasicEnchantment(new EnchantmentSettings()
            .id("flame")
            .maxLevel(1)
            .rarity(20)
            .types(ComplexItem.Type.BOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.ARROW_FIRE));

    public static ComplexEnchantment FORTUNE = new BasicEnchantment(new EnchantmentSettings()
            .id("fortune")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.PICKAXE, ComplexItem.Type.HOE, ComplexItem.Type.SHOVEL, ComplexItem.Type.AXE)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.LOOT_BONUS_BLOCKS)
            .exclusive(EnchantmentWrapper.SILK_TOUCH));

    public static ComplexEnchantment FROST_WALKER = new BasicEnchantment(new EnchantmentSettings()
            .id("frost_walker")
            .maxLevel(2)
            .rarity(10)
            .types(ComplexItem.Type.BOOTS)
            .slots(Slot.FEET)
            .vanillaEnchant(Enchantment.FROST_WALKER)
            .exclusive(EnchantmentWrapper.DEPTH_STRIDER));

    public static ComplexEnchantment IMPALING = new BasicEnchantment(new EnchantmentSettings()
            .id("impaling")
            .maxLevel(5)
            .rarity(20)
            .types(ComplexItem.Type.TRIDENT)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.IMPALING));

    public static ComplexEnchantment INFINITY = new BasicEnchantment(new EnchantmentSettings()
            .id("infinity")
            .maxLevel(1)
            .rarity(10)
            .types(ComplexItem.Type.BOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.ARROW_INFINITE));

    public static ComplexEnchantment KNOCKBACK = new BasicEnchantment(new EnchantmentSettings()
            .id("knockback")
            .maxLevel(2)
            .rarity(20)
            .types(ComplexItem.Type.SWORD)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.KNOCKBACK)
            .exclusive(EnchantmentWrapper.CULLING));

    public static ComplexEnchantment LOOTING = new BasicEnchantment(new EnchantmentSettings()
            .id("looting")
            .maxLevel(3)
            .rarity(10)
            .types(ComplexItem.Type.SWORD)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.LOOT_BONUS_MOBS));

    public static ComplexEnchantment LOYALTY = new BasicEnchantment(new EnchantmentSettings()
            .id("loyalty")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.TRIDENT)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.LOYALTY)
            .exclusive(EnchantmentWrapper.RIPTIDE, EnchantmentWrapper.CHANNELING));

    public static ComplexEnchantment LUCK_OF_THE_SEA = new BasicEnchantment(new EnchantmentSettings()
            .id("luck_of_the_sea")
            .maxLevel(3)
            .rarity(30)
            .types(ComplexItem.Type.FISHING_ROD)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.LUCK));

    public static ComplexEnchantment LURE = new BasicEnchantment(new EnchantmentSettings()
            .id("lure")
            .maxLevel(3)
            .rarity(30)
            .types(ComplexItem.Type.FISHING_ROD)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.LURE));

    public static ComplexEnchantment MULTISHOT = new BasicEnchantment(new EnchantmentSettings()
            .id("multishot")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.CROSSBOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.MULTISHOT)
            .exclusive(EnchantmentWrapper.PIERCING));

    public static ComplexEnchantment PIERCING = new BasicEnchantment(new EnchantmentSettings()
            .id("piercing")
            .maxLevel(4)
            .rarity(20)
            .types(ComplexItem.Type.CROSSBOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.PIERCING)
            .exclusive(EnchantmentWrapper.MULTISHOT));

    public static ComplexEnchantment POWER = new BasicEnchantment(new EnchantmentSettings()
            .id("power")
            .maxLevel(5)
            .rarity(40)
            .types(ComplexItem.Type.BOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.ARROW_DAMAGE));

    public static ComplexEnchantment PROJECTILE_PROTECTION = new BasicEnchantment(new EnchantmentSettings()
            .id("projectile_protection")
            .maxLevel(5)
            .rarity(30)
            .types(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS)
            .slots(Slot.ARMOR)
            .vanillaEnchant(Enchantment.PROTECTION_PROJECTILE)
            .exclusive(EnchantmentWrapper.BLAST_PROTECTION, EnchantmentWrapper.FIRE_PROTECTION, EnchantmentWrapper.PROTECTION));

    public static ComplexEnchantment PROTECTION = new BasicEnchantment(new EnchantmentSettings()
            .id("protection")
            .maxLevel(4)
            .rarity(20)
            .types(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS)
            .slots(Slot.ARMOR)
            .vanillaEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)
            .exclusive(EnchantmentWrapper.BLAST_PROTECTION, EnchantmentWrapper.PROJECTILE_PROTECTION, EnchantmentWrapper.FIRE_PROTECTION));

    public static ComplexEnchantment PUNCH = new BasicEnchantment(new EnchantmentSettings()
            .id("punch")
            .maxLevel(2)
            .rarity(20)
            .types(ComplexItem.Type.BOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.ARROW_KNOCKBACK));

    public static ComplexEnchantment QUICK_CHARGE = new BasicEnchantment(new EnchantmentSettings()
            .id("quick_charge")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.CROSSBOW)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.QUICK_CHARGE));

    public static ComplexEnchantment RESPIRATION = new BasicEnchantment(new EnchantmentSettings()
            .id("respiration")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.HELMET)
            .slots(Slot.HEAD)
            .vanillaEnchant(Enchantment.OXYGEN));

    public static ComplexEnchantment RIPTIDE = new BasicEnchantment(new EnchantmentSettings()
            .id("riptide")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.TRIDENT)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.RIPTIDE)
            .exclusive(EnchantmentWrapper.LOYALTY, EnchantmentWrapper.CHANNELING));

    public static ComplexEnchantment SHARPNESS = new BasicEnchantment(new EnchantmentSettings()
            .id("sharpness")
            .maxLevel(5)
            .rarity(40)
            .types(ComplexItem.Type.SWORD, ComplexItem.Type.AXE, ComplexItem.Type.STAFF)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.DAMAGE_ALL)
            .exclusive(EnchantmentWrapper.SMITE, EnchantmentWrapper.BANE_OF_ARTHROPODS));

    public static ComplexEnchantment SILK_TOUCH = new BasicEnchantment(new EnchantmentSettings()
            .id("silk_touch")
            .maxLevel(1)
            .rarity(20)
            .types(ComplexItem.Type.PICKAXE, ComplexItem.Type.HOE, ComplexItem.Type.SHOVEL, ComplexItem.Type.AXE)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.SILK_TOUCH)
            .exclusive(EnchantmentWrapper.FORTUNE));

    public static ComplexEnchantment SMITE = new BasicEnchantment(new EnchantmentSettings()
            .id("smite")
            .maxLevel(6)
            .rarity(50)
            .types(ComplexItem.Type.SWORD, ComplexItem.Type.AXE, ComplexItem.Type.STAFF)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.DAMAGE_UNDEAD)
            .exclusive(EnchantmentWrapper.SHARPNESS, EnchantmentWrapper.BANE_OF_ARTHROPODS));

    public static ComplexEnchantment SOUL_SPEED = new BasicEnchantment(new EnchantmentSettings()
            .id("soul_speed")
            .maxLevel(3)
            .rarity(0)
            .types(ComplexItem.Type.BOOTS)
            .slots(Slot.FEET)
            .vanillaEnchant(Enchantment.SOUL_SPEED));

    public static ComplexEnchantment SWEEPING_EDGE = new BasicEnchantment(new EnchantmentSettings()
            .id("sweeping_edge")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.SWORD)
            .slots(Slot.MAIN_HAND)
            .vanillaEnchant(Enchantment.SWEEPING_EDGE));

    public static ComplexEnchantment SWIFT_SNEAK = new BasicEnchantment(new EnchantmentSettings()
            .id("swift_sneak")
            .maxLevel(3)
            .rarity(20)
            .types(ComplexItem.Type.LEGGINGS)
            .slots(Slot.LEGS)
            .vanillaEnchant(Enchantment.SWIFT_SNEAK));

    public static ComplexEnchantment THORNS = new BasicEnchantment(new EnchantmentSettings()
            .id("thorns")
            .maxLevel(5)
            .rarity(20)
            .types(ComplexItem.Type.HELMET, ComplexItem.Type.CHESTPLATE, ComplexItem.Type.LEGGINGS, ComplexItem.Type.BOOTS)
            .slots(Slot.ARMOR)
            .vanillaEnchant(Enchantment.THORNS));



    // static function methods

    private static void syphonExecutable(Event e, Integer level, ItemStack item, Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, level));
    }

    private static void darksoulExecutable(Event event, Integer level, ItemStack item, Player p) {
        Random r = new Random();
        // 3-15% chance to...
        if (r.nextDouble() <= level * 0.05) {
            List<Monster> entities = new ArrayList<>(((EntityDamageByEntityEvent) event).getEntity().getNearbyEntities(10, 10, 10).stream()
                    .filter(entity -> entity instanceof Monster)
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
        f.setMetadata("dmgEnv", new FixedMetadataValue(EvoCraft.getPlugin(), false));
        f.setShooter(p);
        f.setYield(yield);
    }


    private static void cullingExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        Entity entity = event.getEntity();
        entity.setVelocity(entity.getVelocity().add(event.getDamager().getLocation().getDirection().normalize().multiply(-1/2f*level)));
    }

    // Snipe - Multiplies projectile damage by 0.005x->0.015x per block of distance between target and player, capped at 0.3x->0.5x
    private static void snipeExecutable(Event e, Integer level, ItemStack item, Player p) {
        EntityDamageByEntityEvent event = ((EntityDamageByEntityEvent) e);
        Entity entity = event.getEntity();
        double distance = p.getLocation().distance(entity.getLocation());
        double buff = Math.min((0.005 * level) * distance, 0.2 + 0.1 * level) + 1;
        event.setDamage(event.getDamage() * buff);
    }

    public static void registerEnchants() {
        Util.logToConsole(ChatColor.WHITE + "Registering " + ChatColor.GOLD + ComplexEnchantment.getRegisteredEnchants().size() + ChatColor.WHITE + " enchantments.");
    }

    enum EnchantmentWrapper {
        AQUA_AFFINITY, BANE_OF_ARTHROPODS,
        BLAST_PROTECTION, CHANNELING,
        DEPTH_STRIDER, EFFICIENCY,
        FEATHER_FALLING, FIRE_ASPECT, FIRE_PROTECTION,
        FORTUNE, FROST_WALKER, IMPALING,
        INFINITY, KNOCKBACK, LOOTING,
        LOYALTY, LUCK_OF_THE_SEA,
        LURE, MULTISHOT,
        PIERCING, POWER,
        PROJECTILE_PROTECTION, PROTECTION,
        PUNCH, QUICK_CHARGE, RESPIRATION,
        RIPTIDE, SHARPNESS, SILK_TOUCH,
        SMITE, SOUL_SPEED, SWEEPING_EDGE,
        SWIFT_SNEAK, THORNS,

        CULLING, DARKSOUL, SIPHON, SNIPE;

        private ComplexEnchantment complexEnchantment;

        EnchantmentWrapper(){
        }

        public ComplexEnchantment getEnchant() {
            return ComplexEnchantment.getRegisteredEnchants().stream()
                    .filter(enchant -> enchant.getId().equals(this.name().toLowerCase()))
                    .findFirst().orElseThrow(() ->
                            new IllegalArgumentException("Enchantment " + this.name() + " not found"));
        }
    }

}